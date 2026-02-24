package com.example.poc.controller;

import com.example.poc.dto.BlogSummarizeRequest;
import com.example.poc.dto.BlogSummarizeResponse;
import com.example.poc.dto.BlogSummarizeRow;
import com.example.poc.service.BlogSummarizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for {@link BlogController}
 */
@ExtendWith(MockitoExtension.class)
public class BlogControllerTest {
    private static final String MOCK_TOKEN_VALUE = "mock-token-value";
    private static final String MOCK_BLOG_URL = "mock-url-value";
    private static final String MOCK_BLOG_ARTICLE = "mock-article-value";
    private static final String MOCK_BLOG_SUMMARIZE = "some-summarize-value";

    @Mock
    private BlogSummarizeService blogSummarizeService;

    private BlogController blogController;

    @BeforeEach
    void setUp() {
        blogController = new BlogController(blogSummarizeService);
    }

    /**
     * Scenario:
     * Executes [{@link BlogController#createSummarize(Jwt, List)}
     * Expectation:
     * A {@link ResponseEntity<BlogSummarizeResponse>} with status 200 should be returned
     */
    @Test
    void whenCreateSummarizeShouldReturnResponseEntityWithOkStatus() {
        Jwt jwt = new Jwt(MOCK_TOKEN_VALUE, Instant.MIN, Instant.MAX, Map.of(".", "."), Map.of("-", "-"));
        BlogSummarizeRequest blogSummarizeRequest = new BlogSummarizeRequest(MOCK_BLOG_URL, MOCK_BLOG_ARTICLE);

        when(blogSummarizeService.createBlogSummarize(any(), any())).thenReturn(new BlogSummarizeResponse(List.of(blogSummarizeRequest), MOCK_BLOG_SUMMARIZE));

        ResponseEntity<?> result = blogController.createSummarize(jwt, List.of(blogSummarizeRequest));

        assertEquals(200, result.getStatusCode().value());

        BlogSummarizeResponse blogSummarizeResponse = (BlogSummarizeResponse) result.getBody();
        assertEquals(MOCK_BLOG_SUMMARIZE, blogSummarizeResponse.summarize());
    }

    /**
     * Scenario:
     * Executes [{@link BlogController#getSummarizeList(Jwt)}
     * Expectation:
     * A {@link ResponseEntity<BlogSummarizeResponse>} with status 200 should be returned
     */
    @Test
    void whenGetSummarizeListShouldReturnResponseEntityWithOkStatus() {
        Jwt jwt = new Jwt(MOCK_TOKEN_VALUE, Instant.MIN, Instant.MAX, Map.of(".", "."), Map.of("-", "-"));

        when(blogSummarizeService.getBlogSummarizeList(any())).thenReturn(List.of(new BlogSummarizeRow(1L, MOCK_BLOG_SUMMARIZE)));

        ResponseEntity<?> result = blogController.getSummarizeList(jwt);

        assertEquals(200, result.getStatusCode().value());

        List<BlogSummarizeRow> blogSummarizeRowList = (List<BlogSummarizeRow>) result.getBody();
        assertEquals(MOCK_BLOG_SUMMARIZE, blogSummarizeRowList.getFirst().summarize());
    }
}