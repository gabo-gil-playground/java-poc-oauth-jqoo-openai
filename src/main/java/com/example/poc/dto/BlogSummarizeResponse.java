package com.example.poc.dto;

import java.util.List;

/**
 * Blog summarize response DTO (using Java record: immutable, concise).
 *
 * @param articles  the {@link List<BlogSummarizeRequest>} original articles urls
 * @param summarize the {@link String} blog summarize from provided articles urls
 */
public record BlogSummarizeResponse(List<BlogSummarizeRequest> articles, String summarize) {
}