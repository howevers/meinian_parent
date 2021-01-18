package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.dao.OrderSettingDao;
import com.myjava.pojo.OrderSetting;
import com.myjava.pojo.OrderSettingExample;
import com.myjava.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 根据日期修改可预约人数
     * @return
     */
    @Override
    public boolean editNumberByDates(OrderSetting orderSetting) {
        if (this.selectCountByOrderDate(orderSetting.getOrderDate())){
            //有值修改
            return this.updateByDate(orderSetting);
        }else {
            //没值添加
            return this.insertByDate(orderSetting);
        }
    }



    /**
     * 根据月份获取数据库的预约信息
     * @param date
     * @return
     */
    @Override
    public List<Map<String, Object>> getOrderSettingByMonth(String date) {
        //查询条件
        String startDate = date + "-1";     //开始日期
        String endDate = date +"-31";       //结束日期
        Map<String, String> map = new HashMap<>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        //根据map集合查找一个月内的数据
        List<OrderSetting> list = orderSettingDao.selectByDates(map);

        //将集合转换返回
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("date",orderSetting.getOrderDate().getDate());
            hashMap.put("number",orderSetting.getNumber());
            hashMap.put("reservations",orderSetting.getReservations());
            arrayList.add(hashMap);
        }
        return arrayList;
    }



    /**
     * 从上传的Excel文件中获取预约信息，并存进数据库中
     * @param settings
     */
    @Override
    public void addBatch(List<OrderSetting> settings) {
        for (OrderSetting setting : settings) {
            if (this.selectCountByOrderDate(setting.getOrderDate())){
                //有值，执行修改操作
                this.updateByDate(setting);
            }else {
                //没有，执行添加操作
                this.insertByDate(setting);
            }
        }
    }


    /**
     * 根据日期判断数据库中是否有值
     * @param date
     * @return
     */
    public boolean selectCountByOrderDate(Date date){
        return (orderSettingDao.selectCountByOrderDate(date)) > 0;
    }


    /**
     * 根据日期修改数据库中数据
     * @param orderSetting
     * @return
     */
    public boolean updateByDate(OrderSetting orderSetting){
        return (orderSettingDao.updateByOrderDate(orderSetting)) > 0;
    }


    /**
     * 新建数据操作
     * @param orderSetting
     * @return
     */
    public  boolean insertByDate(OrderSetting orderSetting){
       return (orderSettingDao.insertSelective(orderSetting)) > 0;
    }
}
