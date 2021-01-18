package com.myjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.entity.Result;
import com.myjava.pojo.TravelItem;
import com.myjava.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelItem")
public class TravelItemController {

    @Reference
    TravelItemService travelItemService;


    /**
     * 获取全部自由行数据 ，用在跟团游新增页面,和编辑跟团游数据回显显示
     * @return
     */
    @RequestMapping("/getAll")
    public Result getAll(){
        List<TravelItem> list = travelItemService.getAll();
        try {
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
     * 编辑自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result update(@RequestBody TravelItem travelItem){
        try {
            if (travelItemService.update(travelItem)){
                return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
            }else {
                return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }


    /**
     * 根据主键id查询一个数据
     * @param id
     * @return
     */
    @RequestMapping("/getById")

    public Result getById(Integer id){
        TravelItem travelItem = travelItemService.getById(id);

        try {
            if (travelItem != null) {
                return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
            }else {
                return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }


    /**
     * 根据主键id删除自由行
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验
    public Result delete(Integer id){

        try {
            if (travelItemService.delete(id)) {
                return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
            }else {
                return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
            }
        } catch (RuntimeException exception) {  // 删除时如果存在关联数据 这里会抛出自定义异常
            exception.printStackTrace();
            return new Result(false,exception.getMessage());
        } catch (Exception exception){
            exception.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }



    /**
     * 带条件分页查询自由行
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")     //权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = travelItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }




    /**
     * 新增自由行  控制器方法
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")     //权限校验
    public Result add(@RequestBody TravelItem travelItem){
        try {
            System.out.println(travelItem);
            travelItemService.add(travelItem);
            //如果方法不抛异常，说明新增成功
            return new Result(true,MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception exception) {
            exception.printStackTrace();
            //捕获异常，新增失败
            return new Result(false,MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }
}
