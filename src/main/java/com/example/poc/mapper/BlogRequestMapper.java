package com.example.poc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BlogRequestMapper {
    @Select("INSERT INTO blog_request(create_user) VALUES(#{user_name}) RETURNING blog_request_id")
    Long insertBlogRequest(@Param("user_name") final String user);

    @Update("UPDATE blog_request SET summarize = (#{summarize}), update_ts = CURRENT_TIMESTAMP WHERE blog_request_id = (#{request_id})")
    void updateBlogRequest(@Param("request_id") final Long requestId, @Param("summarize") final String summarize);
}