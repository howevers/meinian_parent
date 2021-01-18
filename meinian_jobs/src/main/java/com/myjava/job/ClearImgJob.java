package com.myjava.job;

import com.myjava.constant.RedisConstant;
import com.myjava.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    public void clearImg(){


        Jedis jedis = jedisPool.getResource();
        //计算两个redis中的差值，获取垃圾图片信息
        Set<String> sdiff = jedis.sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String temp : sdiff) {
            QiniuUtils.deleteFileFromQiniu(temp);       //删除七牛云中的垃圾数据
            System.out.println("将" + temp + "清除！");
            jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,temp);  //删除redis中的垃圾数据
        }

    }
}
