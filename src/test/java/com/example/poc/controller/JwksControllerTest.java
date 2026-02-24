package com.example.poc.controller;

import com.example.poc.jwt.JwkService;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for {@link JwksController}
 */
@ExtendWith(MockitoExtension.class)
public class JwksControllerTest {

    @Mock
    private JwkService jwkService;

    private JwksController jwksController;

    @BeforeEach
    void setUp() {
        jwksController = new JwksController(jwkService);
    }

    /**
     * Scenario:
     * Executes [{@link JwksController#getJsonWebKeySet()}
     * Expectation:
     * A {@link ResponseEntity<Map<String, Object>>} with status 200 should be returned
     */
    @Test
    void whenGetJsonWebKeySetShouldReturnResponseEntityWithOkStatus() {
        when(jwkService.getJwkSet()).thenReturn(new JWKSet());

        ResponseEntity<Map<String, Object>> result = jwksController.getJsonWebKeySet();

        assertEquals(200, result.getStatusCode().value());
    }
}