package com.example.poc.dto;

/**
 * Token response DTO (using Java record: immutable, concise).
 *
 * @param access_token the {@link String} access token
 * @param token_type   the {@link String} token type
 * @param expires_in   the {@link int} expiration time in seconds
 */
public record TokenResponse(String access_token, String token_type, int expires_in) {
}