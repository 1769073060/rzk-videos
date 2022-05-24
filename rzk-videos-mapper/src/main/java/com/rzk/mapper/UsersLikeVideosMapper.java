package com.rzk.mapper;

import com.rzk.pojo.UsersLikeVideos;
import com.rzk.pojo.UsersLikeVideosExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersLikeVideosMapper {
    int countByExample(UsersLikeVideosExample example);

    int deleteByExample(UsersLikeVideosExample example);

    int deleteByPrimaryKey(String id);

    int insert(UsersLikeVideos record);

    int insertSelective(UsersLikeVideos record);

    List<UsersLikeVideos> selectByExample(UsersLikeVideosExample example);

    UsersLikeVideos selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UsersLikeVideos record, @Param("example") UsersLikeVideosExample example);

    int updateByExample(@Param("record") UsersLikeVideos record, @Param("example") UsersLikeVideosExample example);

    int updateByPrimaryKeySelective(UsersLikeVideos record);

    int updateByPrimaryKey(UsersLikeVideos record);
}