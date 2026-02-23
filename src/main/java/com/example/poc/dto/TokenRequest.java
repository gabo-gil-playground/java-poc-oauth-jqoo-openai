package com.example.poc.dto;

/**
 * Token request DTO (using Java record: immutable, concise).
 *
 * @param subject the {@link String} token subject
 * @param scopes  the {@link String} token scopes
 */
public record TokenRequest(String subject, String scopes) {
}