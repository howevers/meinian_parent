package com.myjava.dao;

import com.myjava.pojo.Member;
import com.myjava.pojo.MemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberDao {
    int countByExample(MemberExample example);

    int deleteByExample(MemberExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Member record);


    /**
     * 添加会员操作，需要主键回填
     * @param record
     * @return
     */
    int insertSelective(Member record);

    List<Member> selectByExample(MemberExample example);

    Member selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Member record, @Param("example") MemberExample example);

    int updateByExample(@Param("record") Member record, @Param("example") MemberExample example);

    int updateByPrimaryKeySelective(Member record);

    /**
     * 根据手机号查询1个会员
     * @param telephone
     * @return
     */
    Member getMemberByTel(String telephone);
}