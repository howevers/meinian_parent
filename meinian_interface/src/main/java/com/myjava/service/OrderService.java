package com.myjava.service;

import com.myjava.entity.Result;

import java.util.Map;

public interface OrderService {

    /**
     * 移动端 下单 方法
     * @param map
     */
    Result subOrder(Map<String,String> map);


    /**
     * 成功下单后查讯订单方法
     * @return
     */
    Map getOrderById(Integer id);
}
