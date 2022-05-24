package com.rzk.mapper;

import com.rzk.pojo.SearchRecords;
import com.rzk.pojo.SearchRecordsExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SearchRecordsMapper {
    int countByExample(SearchRecordsExample example);

    int deleteByExample(SearchRecordsExample example);

    int deleteByPrimaryKey(String id);

    int insert(SearchRecords record);

    int insertSelective(SearchRecords record);

    List<SearchRecords> selectByExample(SearchRecordsExample example);

    SearchRecords selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SearchRecords record, @Param("example") SearchRecordsExample example);

    int updateByExample(@Param("record") SearchRecords record, @Param("example") SearchRecordsExample example);

    int updateByPrimaryKeySelective(SearchRecords record);

    int updateByPrimaryKey(SearchRecords record);

    @Select("select content from search_records group by content order by count(content) desc")

    @Results({
            @Result(property = "content", column = "content"),
    })
    public List<String> getHotselect();
}