package com.example.poc.service;

import com.example.poc.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
@Slf4j
public class BlogAIServiceImpl implements BlogAIService {

    private final ChatModel chatModel;

    /**
     *
     * @param chatModel
     */
    public BlogAIServiceImpl(final ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Summarizes a list of texts content.
     *
     * @param textList the {@link List<String>} to be summarized
     * @return {@link String} summarize of text
     */
    public String summarizeTextList(final List<String> textList) {
        log.debug("summarizeTextList - start");

        String text = String.join("\n", textList);
        log.debug("summarizeTextList - text to summarize: {}", text);

        SystemMessage systemMessage = new SystemMessage(Constants.OPENAI_SYSTEM_PROMPT);
        UserMessage userMessage = new UserMessage(text);

        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
            .model(Constants.OPENAI_MODEL)
            .temperature(Constants.OPENAI_TEMPERATURE)
            .maxTokens(Constants.OPENAI_OUTPUT_MAX_TOKENS)
            .build();

        Prompt prompt = Prompt.builder()
            .messages(systemMessage, userMessage)
            .chatOptions(openAiChatOptions)
            .build();

        ChatResponse chatResponse = chatModel.call(prompt);
        String textResponse = getTextFromChatResponse(chatResponse);

        log.debug("summarizeTextList - text response: {}", textResponse);
        log.debug("summarizeTextList - done");

        return textResponse;
    }

    /**
     *
     * @param chatResponse
     * @return
     */
    private String getTextFromChatResponse(final ChatResponse chatResponse) {
        try {
            if (!chatResponse.getResult().getOutput().getMessageType().equals(MessageType.ASSISTANT)) {
                return Constants.OPENAI_OUTPUT_EMPTY_RESPONSE;
            }

            return chatResponse.getResult().getOutput().getText();
        } catch (Exception e) {
            return Constants.OPENAI_OUTPUT_EMPTY_RESPONSE;
        }
    }
}
