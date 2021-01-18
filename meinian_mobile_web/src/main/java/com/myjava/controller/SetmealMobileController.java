package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.constant.RedisMessageConstant;
import com.myjava.entity.Result;
import com.myjava.pojo.Setmeal;
import com.myjava.service.SetmealService;
import com.myjava.service.TravelGroupService;
import com.myjava.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;




    /**
     * 根据套餐id获取 套餐内的所有信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result getSetmeal(Integer id){

        try {
            Setmeal setmeal = setmealService.getOnly(id);
            if (setmeal != null){
                return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
            }else {
                return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


    /**
     * 查询所有套餐数据 ，用于移动端显示
     * @return
     */
    @RequestMapping("getSetmealAll")
    public Result getSetmealAlls(){
        try {
            List<Setmeal> list = setmealService.getSetmealAlls();
            if (list.isEmpty()){
                return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
            }else {
                return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,list);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
