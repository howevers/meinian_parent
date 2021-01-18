package com.myjava.service;

import com.myjava.pojo.Member;

public interface MemberService {

    /**
     * 查询数据库中会员信息
     * @param telephone
     * @return
     */
    Member getMemberByTelephone(String telephone);

    /**
     * 向数据库添加会员数据
     * @param member
     */
    void setNewMember(Member member);
}
