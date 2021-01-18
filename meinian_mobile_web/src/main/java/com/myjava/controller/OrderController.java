package com.myjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.constant.RedisMessageConstant;
import com.myjava.entity.Result;
import com.myjava.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 成功下单后查讯订单方法
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findByIds(Integer id){
        try {
            Map map = orderService.getOrderById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }



    /**
     * 移动端 下单 方法
     * @param map
     * @return
     */
    @RequestMapping("/submitOrder")
    public Result subOrder(@RequestBody Map<String,String> map){
        try {
            String telephone = map.get("telephone");
            String validateCode = map.get("validateCode");
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //1、验证校验码
            if (code == null || !(code.equals(validateCode))){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //2、保存数据
            return orderService.subOrder(map);
        } catch (RuntimeException e){
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }
}
