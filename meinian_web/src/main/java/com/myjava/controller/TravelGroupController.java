package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.entity.Result;
import com.myjava.pojo.TravelGroup;
import com.myjava.service.TravelGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelgroup")
public class TravelGroupController {

    @Reference  //远程获取业务层
    TravelGroupService travelGroupService;


    /**
     * 删除报团游
     * @param id
     * @return
     */
    @RequestMapping("/deleteTravelgroup")
    @PreAuthorize("hasAuthority('TRAVELGROUP_DELETE')")//权限校验
    public Result deleteTravelgroup(Integer id){
        try {
            if (travelGroupService.deleteTravelgroup(id)){
                return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
            }else {
                return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
            }
        } catch (RuntimeException e){       //删除中间表错误异常
            e.printStackTrace();
            return new Result(false,e.getMessage());
        } catch (Exception exception) {     //其他错误异常
            exception.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }



    /**
     *  编辑修改方法
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @RequestMapping("edit")
    @PreAuthorize("hasAuthority('TRAVELGROUP_EDIT')")//权限校验
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            if (travelGroupService.edit(travelItemIds,travelGroup)){
                return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
            }else {
                return new Result(false,MessageConstant.EDIT_TRAVELGROUP_FAIL);
            }
        } catch (RuntimeException e){   //删除中间表错误异常
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }



    /**
     * 分页有条件查询 跟团信息
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELGROUP_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return travelGroupService.findPage(queryPageBean);
    }

    /**
     * 根据报团游的 id 获取中间表的自由行数据
     * @param id
     * @return
     */
    @RequestMapping("/getTravelgroupAndTravelitem")
    public Result getTravelgroupAndTravelitem(Integer id){
        try {
            List<Integer> list = travelGroupService.getTravelgroupAndTravelitem(id);
            if (list.isEmpty()){
                return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL);
            }else {
                return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,list);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    /**
     * 根据 id 查询一条报团游
     * @param id
     * @return
     */
    @RequestMapping("/getOnly")
    public Result getOnly(Integer id){

        try {
            TravelGroup travelGroup = travelGroupService.getOnly(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }



    /**
     * 新增跟团游
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @RequestMapping("add")
    @PreAuthorize("hasAuthority('TRAVELGROUP_ADD')")//权限校验
    public Result add(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            if (travelGroupService.add(travelItemIds,travelGroup)){
                return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
            }else {
                return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 获取跟团游全部信息
     * @return
     */
    @RequestMapping("/getAll")
    public Result getAll(){
        List<TravelGroup> list = travelGroupService.getAll();
        try {
            if (list.isEmpty()){//判断集合是否为空
                return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
            }else {
                return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
            }
        } catch (Exception exception) {     //如果查询出现异常，也返回失败
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }
}
