package com.example.poc.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogRequestContentMapper {
    @Insert("INSERT INTO blog_request_content(blog_request_id, blog_url, blog_text) VALUES(#{request_id}, #{url}, #{text})")
    void insertBlogRequestContent(@Param("request_id") final Long requestId, @Param("url") final String requestUrl, @Param("text") final String requestText);
}