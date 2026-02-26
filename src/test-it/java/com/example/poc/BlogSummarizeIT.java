package com.example.poc;

import com.example.poc.constant.Constants;
import com.example.poc.dto.BlogSummarizeRequest;
import com.example.poc.dto.BlogSummarizeResponse;
import com.example.poc.dto.BlogSummarizeRow;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.example.poc.jooq.generated.Tables.BLOG_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
public class BlogSummarizeIT {

    private static final String MOCK_JWT_TOKEN_VALUE = "token";
    private static final String MOCK_JWT_CLAIM_SUB_KEY = "sub";
    private static final String MOCK_JWT_CLAIM_SUB_VALUE = "john.doe";
    private static final String MOCK_JWT_HEADER_KEY = "alg";
    private static final String MOCK_JWT_HEADER_VALUE = "none";
    private static final String MOCK_BLOG_SUMMARIZE_ONE = "some-summarize-one-value";
    private static final String MOCK_BLOG_SUMMARIZE_TWO = "some-summarize-two-value";
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DSLContext dslContext;
    @MockitoBean
    private JwtDecoder jwtDecoder;
    @MockitoBean
    private ChatModel chatModel;
    private RestTestClient restTestClient;

    /**
     * Generates valid input tests cases values
     *
     * @return {@link Stream <Arguments>}
     */
    private static Stream<Arguments> validCreateBlogSummarizeInputTestCases() {
        return Stream.of(
            arguments("", ""),
            arguments("http://some-url.xyz", ""),
            arguments("", "some article text"),
            arguments("http://some-url.xyz", "some article text")
        );
    }

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        restTestClient = RestTestClient.bindToApplicationContext(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Scenario:
     * Executes Create Blog Summarize flow with valid content
     * Expectation:
     * Create Blog Summarize flow should be executed
     */
    @ParameterizedTest
    @MethodSource("validCreateBlogSummarizeInputTestCases")
    void whenCreateBlogSummarizeFlowWithValidInput(final String url, final String article) {
        // sets JWT authentication security context
        this.createJwtSecurityContext();

        // mocks open-ai chat response
        AssistantMessage assistantMessage = new AssistantMessage(MOCK_BLOG_SUMMARIZE_ONE);
        List<Generation> generationList = List.of(new Generation(assistantMessage));
        ChatResponse chatResponse = new ChatResponse(generationList);

        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        // creates basic request data
        List<BlogSummarizeRequest> blogSummarizeRequestList = List.of(new BlogSummarizeRequest(url, article));

        // executes endpoint's flow
        BlogSummarizeResponse blogSummarizeResponse = this.restTestClient
            .post()
            .uri(Constants.API_BLOG_PATH + Constants.API_BLOG_SUMMARIZE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(blogSummarizeRequestList)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BlogSummarizeResponse.class)
            .returnResult()
            .getResponseBody();

        assertEquals(blogSummarizeRequestList, blogSummarizeResponse.articles());
        assertEquals(MOCK_BLOG_SUMMARIZE_ONE, blogSummarizeResponse.summarize());
    }

    /**
     * Scenario:
     * Executes Get Blog Summarize List flow with valid content
     * Expectation:
     * Get Blog Summarize List flow should be executed
     */
    @Test
    void whenGetSummarizeListFlowWithValidInput() {
        // sets JWT authentication security context
        this.createJwtSecurityContext();

        // creates basic DB data
        dslContext
            .insertInto(BLOG_REQUEST)
            .columns(BLOG_REQUEST.CREATE_USER, BLOG_REQUEST.SUMMARIZE)
            .values(MOCK_JWT_CLAIM_SUB_VALUE, MOCK_BLOG_SUMMARIZE_ONE)
            .values(MOCK_JWT_CLAIM_SUB_VALUE, MOCK_BLOG_SUMMARIZE_TWO)
            .execute();

        // executes endpoint's flow
        List<BlogSummarizeRow> blogSummarizeRowList = this.restTestClient
            .get()
            .uri(Constants.API_BLOG_PATH + Constants.API_BLOG_SUMMARIZE_PATH)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<List<BlogSummarizeRow>>() {
            })
            .returnResult()
            .getResponseBody();

        assertEquals(2, blogSummarizeRowList.size());
        assertEquals(MOCK_BLOG_SUMMARIZE_ONE, blogSummarizeRowList.getFirst().summarize());
        assertEquals(MOCK_BLOG_SUMMARIZE_TWO, blogSummarizeRowList.getLast().summarize());
    }

    /**
     * Creates {@link SecurityContextHolder} JWT security context for test purposes
     */
    private void createJwtSecurityContext() {
        Map<String, Object> claims = Map.of(MOCK_JWT_CLAIM_SUB_KEY, MOCK_JWT_CLAIM_SUB_VALUE);
        Jwt jwt = Jwt.withTokenValue(MOCK_JWT_TOKEN_VALUE)
            .header(MOCK_JWT_HEADER_KEY, MOCK_JWT_HEADER_VALUE)
            .claims(existingClaims -> existingClaims.putAll(claims))
            .build();

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
