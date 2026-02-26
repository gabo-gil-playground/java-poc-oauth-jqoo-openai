package com.example.poc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for {@link BlogAIServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class BlogAIServiceImplTest {

    private static final String MOCK_BLOG_SUMMARIZE = "some-summarize-value";
    @Mock
    private ChatModel chatModel;
    private BlogAIServiceImpl blogAIService;

    @BeforeEach
    void setUp() {
        blogAIService = new BlogAIServiceImpl(chatModel);
    }

    /**
     * Scenario:
     * Executes [{@link BlogAIServiceImpl#summarizeTextList(List)}]
     * Expectation:
     * A {@link String} with summarize text should be returned
     */
    @Test
    void whenSummarizeTextListShouldReturnSummarizeText() {
        AssistantMessage assistantMessage = new AssistantMessage(MOCK_BLOG_SUMMARIZE);
        List<Generation> generationList = List.of(new Generation(assistantMessage));
        ChatResponse chatResponse = new ChatResponse(generationList);

        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        String result = blogAIService.summarizeTextList(List.of());

        assertEquals(MOCK_BLOG_SUMMARIZE, result);
    }
}