package com.example.poc.dto;

/**
 * Blog brief request DTO (using Java record: immutable, concise).
 *
 * @param url {@link String}
 */
public record BlogBriefRequest(String url) {
}
