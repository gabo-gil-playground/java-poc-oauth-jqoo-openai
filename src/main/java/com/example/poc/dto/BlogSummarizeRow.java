package com.example.poc.dto;

/**
 * Blog summarize row DTO (using Java record: immutable, concise).
 *
 * @param requestId the {@link Long} request id
 * @param summarize the {@link String} blog summarize from provided articles urls
 */
public record BlogSummarizeRow(Long requestId, String summarize) {
}