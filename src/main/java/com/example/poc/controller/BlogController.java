package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.dto.BlogSummarizeRequest;
import com.example.poc.dto.BlogSummarizeResponse;
import com.example.poc.dto.BlogSummarizeRow;
import com.example.poc.service.BlogSummarizeService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Blog controller.
 */
@RestController
@RequestMapping(Constants.API_BLOG_PATH)
@Slf4j
@Validated // validate input parameters
public class BlogController {

    private final BlogSummarizeService blogSummarizeService;

    /**
     * Constructor
     *
     * @param blogSummarizeService {@link BlogSummarizeService}
     */
    public BlogController(final BlogSummarizeService blogSummarizeService) {
        this.blogSummarizeService = blogSummarizeService;
    }

    /**
     * Returns blog summarize based on provided blog urls.
     *
     * @param jwt     the principal authenticator
     * @param request the {@link List<BlogSummarizeRequest>} list of blog urls to generate the summarize
     * @return {@link ResponseEntity<BlogSummarizeResponse>}
     */
    @PostMapping(value = Constants.API_BLOG_SUMMARIZE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogSummarizeResponse> createSummarize(
        @AuthenticationPrincipal final Jwt jwt,
        @RequestBody @NotEmpty(message = "Request can not be empty") final List<BlogSummarizeRequest> request
    ) {
        log.debug("createSummarize - start");
        log.debug("createSummarize - blog summarize for user: {}", jwt.getSubject());

        BlogSummarizeResponse blogSummarizeResponse = blogSummarizeService.createBlogSummarize(jwt.getSubject(), request);

        log.debug("createSummarize - done");

        return ResponseEntity.ok(blogSummarizeResponse);
    }

    /**
     * Returns blog summarize list based on login user.
     *
     * @param jwt the principal authenticator
     * @return {@link ResponseEntity<List<BlogSummarizeRow>>}
     */
    @GetMapping(value = Constants.API_BLOG_SUMMARIZE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogSummarizeRow>> getSummarizeList(@AuthenticationPrincipal final Jwt jwt) {
        log.debug("getSummarizeList - start");
        log.debug("getSummarizeList - blog summarize for user: {}", jwt.getSubject());

        List<BlogSummarizeRow> blogSummarizeRows = blogSummarizeService.getBlogSummarizeList(jwt.getSubject());

        log.debug("getSummarizeList - done");

        return ResponseEntity.ok(blogSummarizeRows);
    }
}