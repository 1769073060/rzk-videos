package com.rzk.mapper;

import com.rzk.pojo.UsersFans;
import com.rzk.pojo.UsersFansExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersFansMapper {
    int countByExample(UsersFansExample example);

    int deleteByExample(UsersFansExample example);

    int deleteByPrimaryKey(String id);

    int insert(UsersFans record);

    int insertSelective(UsersFans record);

    List<UsersFans> selectByExample(UsersFansExample example);

    UsersFans selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UsersFans record, @Param("example") UsersFansExample example);

    int updateByExample(@Param("record") UsersFans record, @Param("example") UsersFansExample example);

    int updateByPrimaryKeySelective(UsersFans record);

    int updateByPrimaryKey(UsersFans record);
}