package com.example.poc.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Blog request content table - MyBatis mapper
 */
@Mapper
public interface BlogRequestContentMapper {

    /**
     * Inserts a new blog request content record based on parameter values
     * @param requestId   {@link Long} the blog request id
     * @param requestUrl  {@link String} the blog request url
     * @param requestText {@link String} the blog request text
     */
    @Insert("INSERT INTO blog_request_content(blog_request_id, blog_url, blog_text) VALUES(#{request_id}, #{url}, #{text})")
    void insertBlogRequestContent(@Param("request_id") final Long requestId, @Param("url") final String requestUrl, @Param("text") final String requestText);
}