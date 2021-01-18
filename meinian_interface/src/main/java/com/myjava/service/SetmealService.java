package com.myjava.service;

import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.pojo.Setmeal;

import java.util.List;

public interface SetmealService {


    /**
     * 添加套餐方法
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    boolean addSetmeal(Integer[] travelgroupIds, Setmeal setmeal);


    /**
     * 分页查询 获取 套餐方法
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据主键 id 查询一个套餐数据
     * @param id
     * @return
     */
    Setmeal getOnly(Integer id);


    /**
     * 根据套餐 id 查询中间表中报团游数据
     * @param id
     * @return
     */
    List<Integer> getSetmealAndTravelgroup(Integer id);

    /**
     * 编辑套餐游
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    boolean edit(Integer[] travelgroupIds, Setmeal setmeal);

    /**
     * 删除套餐游
     * @return
     */
    boolean deleteSetmeal(Integer id);

    /**
     * 查询所有套餐数据 ，用于移动端显示
     * @return
     */
    List<Setmeal> getSetmealAlls();
}
