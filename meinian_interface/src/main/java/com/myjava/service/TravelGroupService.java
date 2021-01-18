package com.myjava.service;

import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {

    /**
     * 新增跟团游
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    boolean add(Integer[] travelItemIds, TravelGroup travelGroup);


    /**
     * 分页有条件查询跟团游
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);


    /**
     * 获取跟团游全部信息
     * @return
     */
    List<TravelGroup> getAll();


    /**
     * 根据 id 查询一条报团游
     * @param id
     * @return
     */
    TravelGroup getOnly(Integer id);


    /**
     * 根据报团游的 id 获取中间表的自由行数据
     * @param id
     * @return
     */
    List<Integer> getTravelgroupAndTravelitem(Integer id);

    /**
     * 编辑修改方法
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    boolean edit(Integer[] travelItemIds, TravelGroup travelGroup);


    /**
     * 删除报团游
     * @param id
     * @return
     */
    boolean deleteTravelgroup(Integer id);
}