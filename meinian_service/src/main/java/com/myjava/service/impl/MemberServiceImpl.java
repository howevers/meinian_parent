package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.dao.MemberDao;
import com.myjava.pojo.Member;
import com.myjava.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {


    @Autowired
    MemberDao memberDao;


    /**
     * 向数据库添加会员数据
     * @param member
     */
    @Override
    public void setNewMember(Member member) {
        memberDao.insertSelective(member);
    }

    /**
     * 查询数据库中会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member getMemberByTelephone(String telephone) {
        return memberDao.getMemberByTel(telephone);
    }
}
