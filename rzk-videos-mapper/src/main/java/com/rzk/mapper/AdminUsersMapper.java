package com.rzk.mapper;

import com.rzk.pojo.AdminUsers;
import com.rzk.pojo.AdminUsersExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUsersMapper {
    int countByExample(AdminUsersExample example);

    int deleteByExample(AdminUsersExample example);

    int deleteByPrimaryKey(String id);

    int insert(AdminUsers record);

    int insertSelective(AdminUsers record);

    List<AdminUsers> selectByExample(AdminUsersExample example);

    AdminUsers selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AdminUsers record, @Param("example") AdminUsersExample example);

    int updateByExample(@Param("record") AdminUsers record, @Param("example") AdminUsersExample example);

    int updateByPrimaryKeySelective(AdminUsers record);

    int updateByPrimaryKey(AdminUsers record);
}