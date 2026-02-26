package com.example.poc.service;

import com.example.poc.dto.BlogSummarizeRequest;
import com.example.poc.dto.BlogSummarizeResponse;
import com.example.poc.dto.BlogSummarizeRow;
import com.example.poc.mapper.BlogRequestContentMapper;
import com.example.poc.mapper.BlogRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.poc.jooq.generated.Tables.BLOG_REQUEST;
import static org.jooq.impl.DSL.table;

/**
 * Blog summarize service
 */
@Service
@Slf4j
public class BlogSummarizeService {

    private final BlogAIServiceImpl blogAIService;
    private final BlogRequestMapper blogRequestMapper;
    private final BlogRequestContentMapper blogRequestContentMapper;
    private final DSLContext dslContext;

    /**
     *
     * @param blogAIService
     * @param blogRequestMapper
     * @param blogRequestContentMapper
     * @param dslContext
     */
    public BlogSummarizeService(final BlogAIServiceImpl blogAIService, final BlogRequestMapper blogRequestMapper, final BlogRequestContentMapper blogRequestContentMapper, final DSLContext dslContext) {
        this.blogAIService = blogAIService;
        this.blogRequestMapper = blogRequestMapper;
        this.blogRequestContentMapper = blogRequestContentMapper;
        this.dslContext = dslContext;
    }

    /**
     *
     * @param user
     * @param blogSummarizeRequests
     * @return
     */
    public BlogSummarizeResponse createBlogSummarize(final String user, final List<BlogSummarizeRequest> blogSummarizeRequests) {
        log.info("createBlogSummarize - start");
        Long requestId = saveRequest(user, blogSummarizeRequests);

        BlogSummarizeResponse blogSummarizeResponse = new BlogSummarizeResponse(blogSummarizeRequests, getSummarize(blogSummarizeRequests));

        saveResponse(requestId, blogSummarizeResponse.summarize());
        log.info("createBlogSummarize - done");

        return blogSummarizeResponse;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<BlogSummarizeRow> getBlogSummarizeList(final String user) {
        log.info("getBlogSummarizeList - start");

        List<BlogSummarizeRow> blogSummarizeRows = this.dslContext
            .select(BLOG_REQUEST.BLOG_REQUEST_ID, BLOG_REQUEST.SUMMARIZE)
            .from(table(BLOG_REQUEST.getName()))
            .where(BLOG_REQUEST.CREATE_USER.eq(user))
            .fetchInto(BlogSummarizeRow.class);

        log.info("getBlogSummarizeList - done");

        return blogSummarizeRows;
    }

    /**
     *
     * @param user
     * @param blogSummarizeRequests
     */
    private Long saveRequest(final String user, final List<BlogSummarizeRequest> blogSummarizeRequests) {
        log.debug("saveRequest - start");

        Long requestId = blogRequestMapper.insertBlogRequest(user);
        blogSummarizeRequests
            .parallelStream()
            .forEach(r -> blogRequestContentMapper.insertBlogRequestContent(requestId, r.url(), r.article()));

        log.debug("saveRequest - done");

        return requestId;
    }

    /**
     *
     * @param requestId
     * @param summarize
     */
    private void saveResponse(final Long requestId, final String summarize) {
        log.debug("saveResponse - start");
        blogRequestMapper.updateBlogRequest(requestId, summarize);
        log.debug("saveResponse - done");
    }

    private String getSummarize(final List<BlogSummarizeRequest> blogSummarizeRequests) {
        log.debug("getSummarize - start");

        List<String> textList = blogSummarizeRequests.stream().map(BlogSummarizeRequest::article).toList();
        String summarize = blogAIService.summarizeTextList(textList);

        log.debug("getSummarize - done");

        return summarize;
    }
}
