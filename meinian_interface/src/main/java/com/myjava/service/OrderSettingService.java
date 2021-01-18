package com.myjava.service;

import com.myjava.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {


    /**
     * 根据月份获取数据库的预约信息
     * @param date
     * @return
     */
    List<Map<String, Object>> getOrderSettingByMonth(String date);



    /**
     * 从上传的Excel文件中获取预约信息，并存进数据库中
     * @param i
     */
    void addBatch(List<OrderSetting> settings);

    /**
     * 根据日期修改可预约人数
     * @return
     */
    boolean editNumberByDates(OrderSetting orderSetting);
}
