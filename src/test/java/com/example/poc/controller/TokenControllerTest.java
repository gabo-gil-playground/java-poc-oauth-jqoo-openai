package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.dto.TokenRequest;
import com.example.poc.dto.TokenResponse;
import com.example.poc.jwt.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for {@link TokenController}
 */
@ExtendWith(MockitoExtension.class)
public class TokenControllerTest {
    private static final String MOCK_SUBJECT = "mock-subject-value";
    private static final String MOCK_SCOPE = "mock-scope-value";
    private static final String MOCK_TOKEN = "some-token-value";

    @Mock
    private JwtTokenService jwtTokenService;

    private TokenController tokenController;

    @BeforeEach
    void setUp() {
        tokenController = new TokenController(jwtTokenService);
    }

    /**
     * Scenario:
     * Executes [{@link TokenController#generateToken(TokenRequest)}
     * Expectation:
     * A {@link ResponseEntity<Object>} with status 200 should be returned
     */
    @Test
    void whenGenerateTokenShouldReturnResponseEntityWithOkStatus() throws Exception {
        when(jwtTokenService.generateToken(any(), any())).thenReturn(MOCK_TOKEN);

        TokenRequest tokenRequest = new TokenRequest(MOCK_SUBJECT, MOCK_SCOPE);
        ResponseEntity<Object> result = tokenController.generateToken(tokenRequest);

        assertEquals(200, result.getStatusCode().value());

        TokenResponse tokenResponse = (TokenResponse) result.getBody();
        assertEquals(MOCK_TOKEN, tokenResponse.access_token());
        assertEquals(Constants.OAUTH_TOKEN_EXPIRES_IN, tokenResponse.expires_in());
        assertEquals(Constants.OAUTH_TOKEN_TYPE_BEARER, tokenResponse.token_type());
    }

    /**
     * Scenario:
     * Executes [{@link TokenController#generateToken(TokenRequest)} when {@link JwtTokenService} throws an exception
     * Expectation:
     * A {@link ResponseEntity<Object>} with status 400 should be returned
     */
    @Test
    void whenGenerateTokenAndJwtTokenServiceFailsShouldReturnResponseEntityWithBadRequestStatus() throws Exception {
        when(jwtTokenService.generateToken(any(), any())).thenThrow(Exception.class);

        TokenRequest tokenRequest = new TokenRequest(MOCK_SUBJECT, MOCK_SCOPE);
        ResponseEntity<Object> result = tokenController.generateToken(tokenRequest);

        assertEquals(400, result.getStatusCode().value());
    }
}