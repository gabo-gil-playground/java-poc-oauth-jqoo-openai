package com.example.poc.service;

import com.example.poc.dto.BlogSummarizeRequest;
import com.example.poc.dto.BlogSummarizeResponse;
import com.example.poc.dto.BlogSummarizeRow;
import com.example.poc.mapper.BlogRequestContentMapper;
import com.example.poc.mapper.BlogRequestMapper;
import org.jooq.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test cases for {@link BlogSummarizeService}
 */
@ExtendWith(MockitoExtension.class)
class BlogSummarizeServiceTest {

    private static final String MOCK_USER = "some-user-value";
    private static final String MOCK_BLOG_URL = "some-url-value";
    private static final String MOCK_BLOG_ARTICLE = "some-text-value";
    private static final String MOCK_BLOG_SUMMARIZE = "some-summarize-value";
    @Mock
    private BlogAIServiceImpl blogAIService;
    @Mock
    private BlogRequestMapper blogRequestMapper;
    @Mock
    private BlogRequestContentMapper blogRequestContentMapper;
    @Mock
    private DSLContext dslContext;
    private BlogSummarizeService blogSummarizeService;

    @BeforeEach
    void setUp() {
        blogSummarizeService = new BlogSummarizeService(blogAIService, blogRequestMapper, blogRequestContentMapper, dslContext);
    }

    /**
     * Scenario:
     * Executes [{@link BlogSummarizeService#createBlogSummarize(String, List)}]
     * Expectation:
     * A {@link BlogSummarizeResponse} with summarized text should be returned
     */
    @Test
    void whenCreateBlogSummarizeShouldReturnSummarizedText() {
        List<BlogSummarizeRequest> blogSummarizeRequestList = List.of(
            new BlogSummarizeRequest(MOCK_BLOG_URL, MOCK_BLOG_ARTICLE),
            new BlogSummarizeRequest(MOCK_BLOG_URL, MOCK_BLOG_ARTICLE)
        );

        // mocks save request behavior
        when(blogRequestMapper.insertBlogRequest(any())).thenReturn(Long.MAX_VALUE);
        doNothing().when(blogRequestContentMapper).insertBlogRequestContent(any(), any(), any());

        // mocks summarize behavior
        when(blogAIService.summarizeTextList(any())).thenReturn(MOCK_BLOG_SUMMARIZE);

        // mocks save response behavior
        doNothing().when(blogRequestMapper).updateBlogRequest(any(), any());

        BlogSummarizeResponse blogSummarizeResponse = blogSummarizeService.createBlogSummarize(MOCK_USER, blogSummarizeRequestList);

        assertEquals(MOCK_BLOG_SUMMARIZE, blogSummarizeResponse.summarize());
    }

    /**
     * Scenario:
     * Executes [{@link BlogSummarizeService#getBlogSummarizeList(String)}]
     * Expectation:
     * A {@link List<BlogSummarizeRow>} with summarized texts should be returned
     */
    @Test
    void whenGetBlogSummarizeListShouldReturnSummarizedTextList() {
        List<BlogSummarizeRow> expectedBlogSummarizeRows = List.of(
            new BlogSummarizeRow(Long.MIN_VALUE, MOCK_BLOG_SUMMARIZE),
            new BlogSummarizeRow(Long.MAX_VALUE, MOCK_BLOG_SUMMARIZE)
        );

        // mocks jooq dsl-context behavior
        SelectSelectStep<Record2<?, ?>> selectSelectStep = mock(SelectSelectStep.class);
        SelectJoinStep<Record2<?, ?>> selectJoinStep = mock(SelectJoinStep.class);
        SelectConditionStep<Record2<?, ?>> selectConditionStep = mock(SelectConditionStep.class);

        when(dslContext.select(any(SelectField.class), any(SelectField.class))).thenReturn(selectSelectStep);
        when(selectSelectStep.from(any(Table.class))).thenReturn(selectJoinStep);
        when(selectJoinStep.where(any(Condition.class))).thenReturn(selectConditionStep);
        when(selectConditionStep.fetchInto(BlogSummarizeRow.class)).thenReturn(expectedBlogSummarizeRows);

        List<BlogSummarizeRow> blogSummarizeList = blogSummarizeService.getBlogSummarizeList(MOCK_USER);

        assertEquals(expectedBlogSummarizeRows.size(), blogSummarizeList.size());
        assertEquals(MOCK_BLOG_SUMMARIZE, blogSummarizeList.getFirst().summarize());
        assertEquals(MOCK_BLOG_SUMMARIZE, blogSummarizeList.getLast().summarize());
    }
}