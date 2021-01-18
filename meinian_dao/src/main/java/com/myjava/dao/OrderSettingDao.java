package com.myjava.dao;

/**
 * 设置查询 预约信息功能
 */

import com.myjava.pojo.OrderSetting;
import com.myjava.pojo.OrderSettingExample;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    int countByExample(OrderSettingExample example);

    int deleteByExample(OrderSettingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderSetting record);

    int insertSelective(OrderSetting record);

    List<OrderSetting> selectByExample(OrderSettingExample example);

    OrderSetting selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderSetting record, @Param("example") OrderSettingExample example);

    int updateByExample(@Param("record") OrderSetting record, @Param("example") OrderSettingExample example);

    int updateByPrimaryKeySelective(OrderSetting record);

    int updateByPrimaryKey(OrderSetting record);

    /**
     * 根据日期判断数据库中是否有值
     * @param date
     * @return
     */
    Integer selectCountByOrderDate(Date date);


    /**
     * 根据map集合查找一个月内的数据
     * @param map
     * @return
     */
    List<OrderSetting> selectByDates(Map<String, String> map);


    /**
     * 根据日期修改数据库中数据  ，修改可预约人数
     * @param orderSetting
     * @return
     */
    int updateByOrderDate(OrderSetting orderSetting);


    /**
     * 修改已预约人数
     * @param orderSetting
     */
    void updateReservationsByOrderDate(OrderSetting orderSetting);


    /**
     * 根据日期获取当天旅行信息
     * @param date
     * @return
     */
    OrderSetting selectByDate(Date date);
}