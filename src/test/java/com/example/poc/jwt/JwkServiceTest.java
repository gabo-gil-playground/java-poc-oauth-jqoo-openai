package com.example.poc.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test cases for {@link JwkService}
 */
@ExtendWith(MockitoExtension.class)
class JwkServiceTest {

    /**
     * Scenario:
     * Executes [{@link JwkService#init()}
     * Expectation:
     * The rsaJwk and jwkSet attributes should be initialized
     */
    @Test
    void whenInitShouldInitializeAttributes() throws Exception {
        JwkService jwkService = new JwkService();
        jwkService.init();

        assertFalse(jwkService.getJwkSet().isEmpty());
        assertNotNull(jwkService.getRsaJwk());
    }
}