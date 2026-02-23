package com.example.poc.dto;

/**
 * Blog summarize request DTO (using Java record: immutable, concise).
 *
 * @param url     the {@link String} blog article url
 * @param article the {@link String} blog article text
 */
public record BlogSummarizeRequest(String url, String article) {
}