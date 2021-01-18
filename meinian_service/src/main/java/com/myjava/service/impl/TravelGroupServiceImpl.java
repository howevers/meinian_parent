package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.constant.MessageConstant;
import com.myjava.dao.TravelGroupDao;
import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.pojo.TravelGroup;
import com.myjava.pojo.TravelgroupExample;
import com.myjava.service.TravelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional  //事务
@Service(interfaceClass = TravelGroupService.class)  //通过接口发布
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelGroupDao;


    /**
     * 删除报团游
     * @param id
     * @return
     */
    @Override
    public boolean deleteTravelgroup(Integer id) {

        //1、先判断套餐表中和跟团游的中间表是否存在关联数据
        if (travelGroupDao.selectSetmealAndTravelgroupCountById(id) > 0){
            //2、存在关联数据抛出异常
            throw new RuntimeException(MessageConstant.DELETE_TRAVELITEMFORID_FAIL);
        }else {
            //3、删除跟团游和自由行的中间表
            if (this.deleteTravelgroupAndTravelitem(id)) {
                //2、删除跟团游表数据
                return (travelGroupDao.deleteByPrimaryKey(id)) > 0;
            } else {
                throw new RuntimeException(MessageConstant.DELETE_ZHONG_JIAN_BIAO_FAIL);
            }
        }
    }


    /**
     * 删除中间表方法
     * @param id
     * @return
     */
    public boolean deleteTravelgroupAndTravelitem(Integer id){
        return (travelGroupDao.deleteTravelgroupAndTravelitem(id)) > 0;
    }


    /**
     * 编辑修改方法
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @Override
    public boolean edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        //1、先删除跟团游的中间表
        if (this.deleteTravelgroupAndTravelitem(travelGroup.getId())){
            //2、重新添加跟团游的中间表
            this.setTravelgroupAndTravelitem(travelItemIds,travelGroup.getId());
            //3、修改跟团游数据
            return (travelGroupDao.updateByPrimaryKeySelective(travelGroup)) > 0;
        }else {
            throw new RuntimeException(MessageConstant.DELETE_ZHONG_JIAN_BIAO_FAIL);
        }
    }


    /**
     * 获取跟团游全部信息
     * @return
     */
    @Override
    public List<TravelGroup> getAll() {
        return travelGroupDao.selectByExample(new TravelgroupExample());
    }


    /**
     * 根据 id 查询一条报团游
     * @param id
     * @return
     */
    @Override
    public TravelGroup getOnly(Integer id) {
        return travelGroupDao.selectByPrimaryKey(id);
    }


    /**
     * 根据报团游的 id 获取中间表的自由行数据
     * @param id
     * @return
     */
    @Override
    public List<Integer> getTravelgroupAndTravelitem(Integer id) {
        return travelGroupDao.getTravelgroupAndTravelitem(id);
    }


    /**
     * 分页有条件查询跟团游
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //开启分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调用方法返回所需数据
        Page page = travelGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 新增跟团游
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @Override
    public boolean add(Integer[] travelItemIds, TravelGroup travelGroup) {
        //添加跟团游数据,需要主键回填
        Integer count = travelGroupDao.insertSelective(travelGroup);

        //向中间表添加数据
        setTravelgroupAndTravelitem(travelItemIds,travelGroup.getId());
        return count > 0;
    }


    /**
     * 向中间表添加数据方法
     * @param travelItemIds
     */
    private void setTravelgroupAndTravelitem(Integer[] travelItemIds, Integer travelGroupId) {
        if (travelItemIds != null && travelItemIds.length > 0){
            for (Integer travelItemId : travelItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("travelItemId",travelItemId);
                map.put("travelGroupId",travelGroupId);
                travelGroupDao.addTravelgroupAndTravelitem(map);
            }
            /*for (Integer travelItemId : travelItemIds) {
                travelGroupDao.addTravelgroupAndTravelitem(travelItemId,travelGroupId);
            }*/
        }
    }
}
