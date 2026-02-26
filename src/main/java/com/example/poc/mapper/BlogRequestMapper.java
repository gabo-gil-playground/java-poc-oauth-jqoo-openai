package com.example.poc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Blog request table - MyBatis mapper
 */
@Mapper
public interface BlogRequestMapper {

    /**
     * Inserts a new blog request record based on parameter values
     *
     * @param user {@link String} the user
     * @return {@link Long} created request id
     */
    @Select("INSERT INTO blog_request(create_user) VALUES(#{user_name}) RETURNING blog_request_id")
    Long insertBlogRequest(@Param("user_name") final String user);

    /**
     *
     * @param requestId {@link Long} the blog request id
     * @param summarize {@link String} the blog summarize
     */
    @Update("UPDATE blog_request SET summarize = (#{summarize}), update_ts = CURRENT_TIMESTAMP WHERE blog_request_id = (#{request_id})")
    void updateBlogRequest(@Param("request_id") final Long requestId, @Param("summarize") final String summarize);
}