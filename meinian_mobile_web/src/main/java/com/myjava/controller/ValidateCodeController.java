package com.myjava.controller;

import com.myjava.constant.MessageConstant;
import com.myjava.constant.RedisMessageConstant;
import com.myjava.entity.Result;
import com.myjava.utils.duanxin.SMSUtils;
import com.myjava.utils.duanxin.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    /**
     * 快捷登录发送验证码方法
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Logins(String telephone){
        try {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("手机号："+ telephone + "，验证码" + code);
            //将验证码放进redis中存放5分钟
            jedisPool.getResource().setex(
                    telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,code);
            //调用工具类发送验证码
            SMSUtils.sendShortMessage(telephone,code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }




    /**
     * 下单时发送验证码方法
     * @param telephone
     * @return
     */
    @RequestMapping("/telephoneCode")
    public Result telephoneCodes(String telephone){
        try {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("手机号："+ telephone + "，验证码" + code);
            //将验证码放进redis中存放5分钟
            jedisPool.getResource().setex(
                    telephone + RedisMessageConstant.SENDTYPE_ORDER,300,code);
            //调用工具类发送验证码
            SMSUtils.sendShortMessage(telephone,code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
