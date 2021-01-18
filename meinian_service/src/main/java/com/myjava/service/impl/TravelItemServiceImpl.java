package com.myjava.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.constant.MessageConstant;
import com.myjava.dao.TravelItemDao;
import com.myjava.entity.PageResult;
import com.myjava.pojo.TravelItem;
import com.myjava.pojo.TravelItemExample;
import com.myjava.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(interfaceClass = TravelItemService.class)
public class TravelItemServiceImpl implements TravelItemService {


    //@Reference
    @Autowired
    TravelItemDao travelItemDao;

    /**
     * 新增自由行
     * @param travelItem
     */
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.insertSelective(travelItem);
    }


    /**
     * 带条件分页查询自由行
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        /*TravelItemExample example = new TravelItemExample();
        TravelItemExample.Criteria criteria = example.createCriteria();*/
        //逆向工程生成方法不满足使用条件
        //List<TravelItem> travelItems = travelItemDao.selectByExample(example);

        //1、开启分页
        PageHelper.startPage(currentPage,pageSize);
        Page<TravelItem> page = travelItemDao.findPage(queryString);

        //2、封装分页对象并返回
        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 根据主键删除自由行
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {

        //查询中间表中是否有值，有值就抛自定义异常
        if (travelItemDao.findCountByTravelItemId(id) > 0){
            throw new RuntimeException(MessageConstant.DELETE_TRAVELITEMFORID_FAIL);
        }
        return (travelItemDao.deleteByPrimaryKey(id)) > 0;
    }

    /**
     * 根据主键id查询一个数据
     * @param id
     * @return
     */
    @Override
    public TravelItem getById(Integer id) {
        return travelItemDao.selectByPrimaryKey(id);
    }


    /**
     * 编辑自由行
     * @param travelItem
     * @return
     */
    @Override
    public boolean update(TravelItem travelItem) {
        return (travelItemDao.updateByPrimaryKeySelective(travelItem)) > 0;
    }


    /**
     * 获取全部自由行数据
     * @return
     */
    @Override
    public List<TravelItem> getAll() {
        return travelItemDao.selectByExample(new TravelItemExample());
    }
}
