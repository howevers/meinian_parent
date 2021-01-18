package com.myjava.service;

import com.myjava.entity.PageResult;
import com.myjava.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {

    /**
     * 新增自由行
     * @param travelItem
     */
    void add(TravelItem travelItem);


    /**
     * 带条件分页查询自由行
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);


    /**
     * 根据主键删除自由行
     * @param id
     * @return
     */
    boolean delete(Integer id);

    /**
     * 根据主键id查询一个数据
     * @param id
     * @return
     */
    TravelItem getById(Integer id);

    /**
     * 编辑自由行
     * @param travelItem
     * @return
     */
    boolean update(TravelItem travelItem);


    /**
     * 获取全部自由行数据
     * @return
     */
    List<TravelItem> getAll();
}
