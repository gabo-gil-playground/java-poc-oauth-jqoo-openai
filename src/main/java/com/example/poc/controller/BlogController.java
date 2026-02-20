package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.dto.BlogBriefRequest;
import com.example.poc.dto.BlogBriefResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Development-only controller that issues signed JWTs for local testing.
 * <p>
 * Protect with profile "dev" so it isn't available in non-dev environments.
 * The endpoint returns a JSON object similar to an OAuth2 token response.
 * <p>
 * NOTE: For production, tokens should be issued by a proper Authorization Server.
 */
@RestController
@RequestMapping(Constants.API_BLOG_PATH)
@Slf4j
public class BlogController {

    /**
     * Issue an access token for POC / local testing.
     * <p>
     * Example request body:
     * { "subject": "client1", "scopes": "read write" }
     * <p>
     * Response:
     * { "access_token": "...", "token_type": "bearer", "expires_in": 3600 }
     */
    @GetMapping(value = Constants.API_BLOG_BRIEF_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogBriefResponse> generateBrief(@AuthenticationPrincipal Jwt jwt, @RequestBody List<BlogBriefRequest> request) throws Exception {
        log.info("generateBrief - blog brief for urls: {}", request);

        return ResponseEntity.ok(new BlogBriefResponse(request, "to be implemented"));
    }
}
