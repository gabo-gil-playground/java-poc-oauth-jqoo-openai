package com.example.poc.dto;

/**
 * Token request DTO (using Java record: immutable, concise).
 *
 * @param subject {@link String}
 * @param scopes  {@link String}
 */
public record TokenRequest(String subject, String scopes) {
}
