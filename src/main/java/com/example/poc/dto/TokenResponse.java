package com.example.poc.dto;

/**
 * Token response DTO (using Java record: immutable, concise).
 *
 * @param access_token {@link String}
 * @param token_type   {@link String}
 * @param expires_in   {@link int}
 */
public record TokenResponse(String access_token, String token_type, int expires_in) {
}
