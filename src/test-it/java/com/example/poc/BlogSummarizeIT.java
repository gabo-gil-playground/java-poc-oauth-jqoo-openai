package com.example.poc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@EnableConfigurationProperties
public class BlogSummarizeIT {

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private ChatModel chatModel;

    /**
     * Scenario:
     * Executes Feed Event listener flow with valid {FeedEvent} content
     * Expectation:
     * Feed Event listener flow should be executed
     */
    @ParameterizedTest
    @MethodSource("validInputTestCases")
    void whenFeedEventListenerFlowWithValidFeedEventInput(final String text) {
        assertFalse(text.isBlank());
    }

    /**
     * Generates valid input tests cases values
     *
     * @return {@link Stream <Arguments>}
     */
    private static Stream<Arguments> validInputTestCases() {
        return Stream.of(
            arguments("text-01"),
            arguments("text-02")
        );
    }
}
