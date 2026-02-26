package com.example.poc.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Blog summarize request DTO (using Java record: immutable, concise).
 *
 * @param url     the {@link String} blog article url
 * @param article the {@link String} blog article text
 */
public record BlogSummarizeRequest(
    @NotBlank(message = "URL can not be blank or empty") String url,
    @NotBlank(message = "Article can not be blank or empty") String article
) {
}