package com.example.poc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test cases for {@link HealthController}
 */
@ExtendWith(MockitoExtension.class)
public class HealthControllerTest {

    /**
     * Scenario:
     * Executes [{@link HealthController#healthCheck()}
     * Expectation:
     * A {@link ResponseEntity<Object>} with status 200 should be returned
     */
    @Test
    void whenHealthCheckShouldReturnResponseEntityWithOkStatus() {
        ResponseEntity<Object> result = new HealthController().healthCheck();
        assertEquals(200, result.getStatusCode().value());
    }
}