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

        assertEquals("/health", Constants.API_HEALTH_PATH);
        assertEquals("/api/v1", Constants.API_MAIN_PATH);
        assertEquals("/api/v1/blog", Constants.API_BLOG_PATH);
        assertEquals("/summarize", Constants.API_BLOG_SUMMARIZE_PATH);
        assertEquals("/.well-known/jwks.json", Constants.API_WELL_KNOWN_JSON_PATH);
        assertEquals("/token/generate", Constants.API_TOKEN_GENERATE_PATH);

        assertEquals("SCOPE_", Constants.OAUTH_SCOPE_PREFIX);
        assertEquals("scope", Constants.OAUTH_SCOPE_CLAIM_NAME);
        assertEquals("SCOPE_read", Constants.OAUTH_SCOPE_READ);
        assertEquals("bearer", Constants.OAUTH_TOKEN_TYPE_BEARER);
        assertEquals(3600, Constants.OAUTH_TOKEN_EXPIRES_IN);

        assertEquals("gpt-4.1-nano", Constants.OPENAI_MODEL);
        assertEquals(0.2, Constants.OPENAI_TEMPERATURE);
        assertEquals(200, Constants.OPENAI_OUTPUT_MAX_TOKENS);
        assertEquals("Empty response", Constants.OPENAI_OUTPUT_EMPTY_RESPONSE);
        assertEquals("You are a Software Engineer at SME with a strong ability and experience to identify the main points of a concept. Summarize the following text for a senior developer in 2-3 lines. Maintain a technical and concise tone without adding or inventing content.", Constants.OPENAI_SYSTEM_PROMPT);
    }
}