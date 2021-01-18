package com.myjava.dao;

import com.github.pagehelper.Page;
import com.myjava.pojo.TravelItem;
import com.myjava.pojo.TravelItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelItemDao {
    int countByExample(TravelItemExample example);

    int deleteByExample(TravelItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TravelItem record);

    int insertSelective(TravelItem record);

    List<TravelItem> selectByExample(TravelItemExample example);

    TravelItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TravelItem record, @Param("example") TravelItemExample example);

    int updateByExample(@Param("record") TravelItem record, @Param("example") TravelItemExample example);

    int updateByPrimaryKeySelective(TravelItem record);

    int updateByPrimaryKey(TravelItem record);


    /**
     * 自己写的查询sql，模糊查询项目编码或者项目名称
     * @param queryString
     * @return
     */
    Page<TravelItem> findPage(String queryString);

    /**
     * 自由行中：查询中间表是否存在关联数据
     * @param travelItemId
     * @return
     */
    Integer findCountByTravelItemId(Integer travelItemId);

    /**
     * 根据跟团游id查询所有自由行
     * @param id
     * @return
     */
    List<TravelItem> selectTravelItemByTravelGroupId(Integer id);

}
