package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.constant.RedisConstant;
import com.myjava.dao.SetmealDao;
import com.myjava.entity.PageResult;
import com.myjava.entity.QueryPageBean;
import com.myjava.pojo.Setmeal;
import com.myjava.pojo.SetmealExample;
import com.myjava.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;



    /**
     * 查询所有套餐数据 ，用于移动端显示
     * @return
     */
    @Override
    public List<Setmeal> getSetmealAlls() {
        return setmealDao.selectByExample(new SetmealExample());
    }



    /**
     * 删除redis中的数据   删除和编辑操作时使用
     * @param id
     */
    public void deleteRedis(Integer id){
        //获取被删除套餐表的img属性值
        Setmeal only = this.getOnly(id);
        //将redis中的数据删除
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, only.getImg());
    }

    /**
     * 向数据库添加数据时同时也向redis中储存相应的文件名 解决垃圾图片问题
     * @param fileName
     */
    public void addRedis(String fileName){
        //这个redis中存的不是垃圾图片了
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }



    /**
     * 删除套餐游
     * @return
     */
    @Override
    public boolean deleteSetmeal(Integer id) {
        //1、删除中间表数据
        setmealDao.deleteSetmealAndTravelgroupBySetmealId(id);
        //2、删除套餐表数据
        //2-1、将DB redis中的数据删除(真实数据)
        this.deleteRedis(id);

        return setmealDao.deleteByPrimaryKey(id) > 0;
    }



    /**
     * 编辑套餐游
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    @Override
    public boolean edit(Integer[] travelgroupIds, Setmeal setmeal) {
        //1、删除中间表数据
        setmealDao.deleteSetmealAndTravelgroupBySetmealId(setmeal.getId());
        //2、添加中间表数据
        this.addSetmealAndTravelgroup(travelgroupIds,setmeal.getId());
        //3、修改套餐表数据
        //3-1、先将DB redis中的数据删除（真实数据）
        this.deleteRedis(setmeal.getId());
        //3-2、将表单提交的数据写入DB redis中（真实数据）
        this.addRedis(setmeal.getImg());
        return setmealDao.updateByPrimaryKeySelective(setmeal) > 0;
    }


    /**
     * 根据套餐 id 查询中间表中报团游数据
     * @param id
     * @return
     */
    @Override
    public List<Integer> getSetmealAndTravelgroup(Integer id) {
        return setmealDao.getSetmealAndTravelgroup(id);
    }

    /**
     * 根据主键 id 查询一个套餐数据
     * @param id
     * @return
     */
    @Override
    public Setmeal getOnly(Integer id) {
        return setmealDao.selectByPrimaryKey(id);
    }

    /**
     * 分页查询 获取 套餐方法
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page page = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加套餐方法
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    @Override
    public boolean addSetmeal(Integer[] travelgroupIds, Setmeal setmeal) {

        //1、先在setmeal表中添加数据 并获取主键id
        int count = setmealDao.insertSelective(setmeal);
        //2、向中间表中添加数据
        if (travelgroupIds != null && travelgroupIds.length > 0) {
            this.addSetmealAndTravelgroup(travelgroupIds, setmeal.getId());
        }
        /*解决垃圾图片问题*/
        this.addRedis(setmeal.getImg());
        return count > 0;
    }


    /**
     * 向中间表中添加数据
     * @param travelgroupIds
     * @param setmealId
     */
    public void addSetmealAndTravelgroup(Integer[] travelgroupIds,Integer setmealId){
        for (Integer travelgroupId : travelgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmealId",setmealId);
            map.put("travelgroupId",travelgroupId);
            setmealDao.addSetmealAndTravelgroup(map);
        }
    }
}
