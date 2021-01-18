package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.constant.RedisConstant;
import com.myjava.entity.FileName;
import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.entity.Result;
import com.myjava.pojo.Setmeal;
import com.myjava.service.SetmealService;
import com.myjava.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;


    /**
     * 删除套餐游
     * @param id
     * @return
     */
    @RequestMapping("/deleteSetmeal")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")//权限校验
    public Result deleteSetmealById(Integer id){
        try {
            if (setmealService.deleteSetmeal(id)){
                return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
            }else {
                return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }


    /**
     * 编辑套餐游
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    @RequestMapping("/editBySetmealId")
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")//权限校验
    public Result edit(Integer[] travelgroupIds , @RequestBody Setmeal setmeal){
        try {
            if (setmealService.edit(travelgroupIds,setmeal)){
                return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
            }else {
                return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }



    /**
     * 根据套餐 id 查询中间表中报团游数据
     * @param id
     * @return
     */
    @RequestMapping("/getSetmealAndTravelgroup")
    public Result getSetmealAndTravelgroup(Integer id){
        try {
            List<Integer> list = setmealService.getSetmealAndTravelgroup(id);
            if (list.isEmpty()){
                return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
            }else {
                return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }



    /**
     * 根据主键 id 查询一个套餐数据
     * @param id
     * @return
     */
    @RequestMapping("/getOnly")
    public Result getOnly(Integer id){
        try {
            Setmeal setmeal = setmealService.getOnly(id);
            if (setmeal != null){
                return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
            }else {
                return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    /**
     * 添加套餐方法
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")//权限校验
    public Result addSetmeal(Integer[] travelgroupIds, @RequestBody Setmeal setmeal){
        try {
            if (setmealService.addSetmeal(travelgroupIds,setmeal)) {
                return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
            }else {
                return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询 获取 套餐方法
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }


    /**
     * 处理上传
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try {
            String filename = imgFile.getOriginalFilename();        //原始文件名
            String sub = filename.substring(filename.lastIndexOf("."));     //获取文件后缀名
            filename = UUID.randomUUID().toString() + sub;          //生成随机文件名
            filename = filename.replaceAll("-", "");        //将文件名中的 - 去掉

            //上传到青牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);
            /*解决垃圾图片问题*/
            Jedis resource = jedisPool.getResource();   //从连接池获取连接
            resource.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);  //将文件名放进redis中（有可能是垃圾文件）
            /* ************* */
            String filenames = "http://qmm5xt3gd.hn-bkt.clouddn.com/" + filename ;
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,new FileName(filename,filenames));
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
