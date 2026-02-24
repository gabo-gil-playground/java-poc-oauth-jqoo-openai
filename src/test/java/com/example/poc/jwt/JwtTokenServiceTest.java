package com.example.poc.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for {@link JwtTokenService}
 */
@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    private JwtTokenService jwtTokenService;

    private static final String MOCK_TOKEN_USER = "mock-user-value";
    private static final String MOCK_TOKEN_SCOPE = "SCOPE_read";

    @BeforeEach
    void setUp() throws Exception {
        JwkService jwkService = new JwkService();
        jwkService.init();

        jwtTokenService = new JwtTokenService(jwkService);
    }

    /**
     * Scenario:
     * Executes [{@link JwtTokenService#generateToken(String, String)}
     * Expectation:
     * A {@link String} token should be returned
     */
    @Test
    void whenGenerateTokenShouldReturnToken() throws Exception {
        String token = jwtTokenService.generateToken(MOCK_TOKEN_USER, MOCK_TOKEN_SCOPE);
        assertFalse(token.isEmpty());
    }
}