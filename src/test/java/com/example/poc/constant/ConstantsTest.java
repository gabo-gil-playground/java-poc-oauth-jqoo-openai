package com.example.poc.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test cases for {@link Constants}
 */
public class ConstantsTest {

    /**
     * Scenario:
     * Gets {@link Constants} all defined constant fixed values
     * Expectation:
     * All constant fixed values should be retrieved
     */
    @Test
    void when_constants_should_return_values() {
        assertEquals("status", Constants.APP_HEALTH_MESSAGE_KEY);
        assertEquals("server is running", Constants.APP_HEALTH_MESSAGE_VALUE);
    }
}