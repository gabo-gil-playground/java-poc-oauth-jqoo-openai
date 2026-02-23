package com.example.poc.service;

import java.util.List;

/**
 *
 */
public interface BlogAIService {

    /**
     * Summarizes a list of texts content.
     *
     * @param textList the {@link List <String>} to be summarized
     * @return {@link String} summarize of text
     */
    String summarizeTextList(final List<String> textList);
}
