package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.entity.Result;
import com.myjava.pojo.OrderSetting;
import com.myjava.service.OrderSettingService;
import com.myjava.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    @PreAuthorize("hasAuthority('ORDERSETTING')")//权限校验
    public Result editNumberByDates(@RequestBody OrderSetting orderSetting){
        try {
            if (orderSettingService.editNumberByDates(orderSetting)){
                return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
            }else {
                return new Result(false,MessageConstant.ORDERSETTING_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }




    /**
     * 根据月份获取数据库的预约信息
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    @PreAuthorize("hasAuthority('ORDERSETTING')")//权限校验
    public Result getOrderSettingMonths(String date){
        try {
            List<Map<String,Object>> list = orderSettingService.getOrderSettingByMonth(date);
            if (list.isEmpty()){
                return new Result(false,MessageConstant.QUERY_ORDER_FAIL_NULL);
            }else {
                return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }


    /**
     * 从上传的Excel文件中获取预约信息，并存进数据库中
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @PreAuthorize("hasAuthority('ORDERSETTING')")//权限校验
    public Result uploadEx(@RequestParam("excelFile") MultipartFile file){
        try {
            List<String[]> list = POIUtils.readExcel(file);
            List<OrderSetting> settings = new ArrayList<>();
            for (String[] strs : list) {
                    String str1 = strs[0];
                    String str2 = strs[1];
                OrderSetting orderSetting = new OrderSetting(new Date(str1), Integer.parseInt(str2));
                settings.add(orderSetting);
            }
            orderSettingService.addBatch(settings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
}
