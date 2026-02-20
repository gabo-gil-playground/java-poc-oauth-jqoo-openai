package com.example.poc.dto;

import java.util.List;

/**
 * Blog brief request DTO (using Java record: immutable, concise).
 *
 * @param articles {@link List<BlogBriefRequest>}
 * @param brief    {@link String}
 */
public record BlogBriefResponse(List<BlogBriefRequest> articles, String brief) {
}
