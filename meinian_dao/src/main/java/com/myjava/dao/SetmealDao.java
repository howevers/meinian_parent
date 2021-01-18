package com.myjava.dao;

import com.github.pagehelper.Page;
import com.myjava.pojo.Setmeal;
import com.myjava.pojo.SetmealExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    int countByExample(SetmealExample example);

    int deleteByExample(SetmealExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Setmeal record);

    int insertSelective(Setmeal record);

    List<Setmeal> selectByExample(SetmealExample example);

    Setmeal selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Setmeal record, @Param("example") SetmealExample example);

    int updateByExample(@Param("record") Setmeal record, @Param("example") SetmealExample example);

    int updateByPrimaryKeySelective(Setmeal record);

    int updateByPrimaryKey(Setmeal record);

    /**
     * 向中间表中添加数据
     * @param map
     */
    void addSetmealAndTravelgroup(Map<String, Integer> map);

    /**
     * 分页查询 获取 套餐方法
     * @param queryString
     * @return
     */
    Page findPage(String queryString);

    /**
     * 根据套餐 id 查询中间表中报团游数据
     * @param id
     * @return
     */
    List<Integer> getSetmealAndTravelgroup(Integer id);

    /**
     * 删除中间表数据
     * @param id
     */
    void deleteSetmealAndTravelgroupBySetmealId(Integer id);
}
