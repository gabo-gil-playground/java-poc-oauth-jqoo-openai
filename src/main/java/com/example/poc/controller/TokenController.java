package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.dto.TokenRequest;
import com.example.poc.dto.TokenResponse;
import com.example.poc.jwt.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Development-only controller that issues signed JWTs for local testing.
 * <p>
 * Protect with profile "dev" so it isn't available in non-dev environments.
 * The endpoint returns a JSON object similar to an OAuth2 token response.
 * <p>
 * NOTE: For production, tokens should be issued by a proper Authorization Server.
 */
@Profile("!uat & !staging & !prod")
@RestController
@Slf4j
public class TokenController {
    private final JwtTokenService jwtTokenService;

    /**
     *
     * @param jwtTokenService
     */
    public TokenController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Issue an access token for POC / local testing.
     * <p>
     * Example request body:
     * { "subject": "client1", "scopes": "read write" }
     * <p>
     * Response:
     * { "access_token": "...", "token_type": "bearer", "expires_in": 3600 }
     */
    @PostMapping(value = Constants.API_TOKEN_GENERATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object generateToken(@RequestBody TokenRequest request) {
        log.info("generateToken - new token request for user: {} - scope: {}", request.subject(), request.scopes());

        try {
            String token = jwtTokenService.generateToken(request.subject(), request.scopes());
            return ResponseEntity.ok(new TokenResponse(token, Constants.OAUTH_TOKEN_TYPE_BEARER, Constants.OAUTH_TOKEN_EXPIRES_IN));
        } catch (Exception e) {
            log.error("generateToken - generate error: {}", e.getMessage());
            return ResponseEntity.badRequest();
        }
    }
}
