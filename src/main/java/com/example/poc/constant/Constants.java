package com.example.poc.constant;

/**
 * Application constants values (avoid hard-coded text, magic numbers and repeated values)
 */
public interface Constants {
    String APP_HEALTH_MESSAGE_KEY = "status";
    String APP_HEALTH_MESSAGE_VALUE = "server is running";

    String API_HEALTH_PATH = "/health";
    String API_MAIN_PATH = "/api/v1";
    String API_BLOG_PATH = API_MAIN_PATH + "/blog";
    String API_BLOG_BRIEF_PATH = "/brief";
    String API_WELL_KNOWN_JSON_PATH = "/.well-known/jwks.json";
    String API_TOKEN_GENERATE_PATH = "/token/generate";

    String OAUTH_SCOPE_PREFIX = "SCOPE_";
    String OAUTH_SCOPE_CLAIM_NAME = "scope";
    String OAUTH_SCOPE_READ = "SCOPE_read";
    String OAUTH_TOKEN_TYPE_BEARER = "bearer";
    int OAUTH_TOKEN_EXPIRES_IN = 3600;
}
