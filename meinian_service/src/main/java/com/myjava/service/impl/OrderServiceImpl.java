package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.constant.MessageConstant;
import com.myjava.dao.MemberDao;
import com.myjava.dao.OrderDao;
import com.myjava.dao.OrderSettingDao;
import com.myjava.entity.Result;
import com.myjava.pojo.Member;
import com.myjava.pojo.Order;
import com.myjava.pojo.OrderExample;
import com.myjava.pojo.OrderSetting;
import com.myjava.service.OrderService;
import com.myjava.utils.duanxin.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;  //旅行团订单(预约)信息
    @Autowired
    MemberDao memberDao;    //旅行团会员
    @Autowired
    OrderSettingDao orderSettingDao;//查询、设置预约信息


    /**
     * 成功下单后查讯订单方法
     * @return
     */
    @Override
    public Map getOrderById(Integer id) {
        return orderDao.getOrderById(id);
    }


    /**
     * 移动端 下单 方法
     * @param map
     */
    @Override
    public Result subOrder(Map<String,String> map) {
        //1、判断当前日期能否预约
        String orderDate = map.get("orderDate");    //日期
        String telephone = map.get("telephone");    //手机号
        String setmealId = map.get("setmealId");    //旅行套餐ID
        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);//转换日期
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //获取数据库中那天的预约信息
        OrderSetting orderSetting = orderSettingDao.selectByDate(date);
        if (orderSetting == null){   //判断日期是否存在
            throw new RuntimeException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、判断预约日期是否已满
        if (orderSetting.getReservations() >= orderSetting.getNumber()){
            throw new RuntimeException(date + "：预约已满！");
        }

        //3、查询是否是已经注册，如果没有注册，直接插入
        Member member = memberDao.getMemberByTel(telephone); //根据手机号查询1个会员
        if (member == null){
            member = new Member();
            member.setName(map.get("name"));    //姓名
            member.setSex(map.get("sex"));      //性别
            member.setIdCard(map.get("idCard"));    //身份证号
            member.setPhoneNumber(telephone);   //手机号
            member.setRegTime(new Date());  //注册时间
            //添加会员操作
            memberDao.insertSelective(member);
        }else {
            //3-1、判断会员是否已经预约
            OrderExample example = new OrderExample();
            OrderExample.Criteria criteria = example.createCriteria();
            criteria.andMemberIdEqualTo(member.getId());  //按照会员id查找
            criteria.andOrderdateEqualTo(date);     //按照日期查找
            criteria.andSetmealIdEqualTo(Integer.parseInt(setmealId));      //按照套餐ID查找
            //调用方法查询数据库
            List<Order> orders = orderDao.selectByExample(example);
            if (!(orders.isEmpty())){   //如果已经预约就会抛出异常
                throw new RuntimeException(MessageConstant.HAS_ORDERED);
            }
        }

        //4、进行预约
        Order order = new Order(member.getId(), date,
                Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, Integer.parseInt(setmealId));
        orderDao.insertSelective(order);    //添加订单信息
        //4-1、人数 + 1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.updateReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }
}
