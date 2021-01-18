package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.constant.MessageConstant;
import com.myjava.constant.RedisMessageConstant;
import com.myjava.entity.Result;
import com.myjava.pojo.Member;
import com.myjava.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/login")
public class LoginController {

    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;


    /**
     * 手机端快捷登录的验证码校验方法
     * 并且查询是否已经注册过会员
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result logins(Map<String,String> map , HttpServletResponse httpServletResponse){
        //校验验证码
        String telephone = map.get("telephone");
        String validateCode = map.get("validateCode");
        //从redis中获取验证码信息
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (code != null && code.equals(validateCode)){
            //查询数据库中会员信息
            Member member = memberService.getMemberByTelephone(telephone);
            if (member == null){    //如果不存在就注册一个
                member = new Member();
                member.setRegTime(new Date());  //注册时间
                member.setPhoneNumber(telephone);   //手机号
                memberService.setNewMember(member); //向数据库添加会员数据
            }
            //登陆成功，像浏览器写入Cookie
            Cookie cookie = new Cookie("login_telephone",telephone);
            cookie.setPath("/");    //访问所有时，都要带着cookie
            cookie.setMaxAge(60*60*24*366); //cookie有效时间366天
            httpServletResponse.addCookie(cookie);


            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else {
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
