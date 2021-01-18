package com.myjava.dao;

import com.github.pagehelper.Page;
import com.myjava.pojo.TravelGroup;
import com.myjava.pojo.TravelgroupExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    int countByExample(TravelgroupExample example);

    int deleteByExample(TravelgroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TravelGroup record);

    /**
     * 添加跟团游数据,需要主键回填
     * @param record
     * @return
     */
    int insertSelective(TravelGroup record);

    List<TravelGroup> selectByExample(TravelgroupExample example);

    TravelGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TravelGroup record, @Param("example") TravelgroupExample example);

    int updateByExample(@Param("record") TravelGroup record, @Param("example") TravelgroupExample example);

    int updateByPrimaryKeySelective(TravelGroup record);

    int updateByPrimaryKey(TravelGroup record);



    /**
     * 向中间表添加数据方法
     */
 //   void addTravelgroupAndTravelitem(@Param("id1")Integer travelItemId, @Param("id2") Integer travelGroupId);
    void addTravelgroupAndTravelitem(Map<String,Integer> map);


    /**
     * 分页有条件查询跟团游
     * @param queryString
     * @return
     */

    Page findPage(String queryString);

    /**
     * 根据报团游的 id 获取中间表的自由行数据
     * @param id
     * @return
     */
    List<Integer> getTravelgroupAndTravelitem(Integer id);



    /**
     * 根据跟团游id删除中间表数据
     * @param id
     * @return
     */
    Integer deleteTravelgroupAndTravelitem(Integer id);

    /**
     * 判断套餐表中和跟团游的中间表是否存在关联数据
     * @param id
     * @return
     */
    Integer selectSetmealAndTravelgroupCountById(Integer id);


    /**
     * 根据 套餐id 获取 所有跟团游数据
     * @param id
     * @return
     */
    List<TravelGroup> selectTravelGroupBySetmealId(Integer id);
}
