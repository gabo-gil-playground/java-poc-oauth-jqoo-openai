# java-poc-oauth-jooq-openai
## Overview
Basic Java project skeleton with Gradle, Java 25 and Spring Boot 4.0.3. Pre-configured with:

- OAuth2 Security: Resource Server with local JWKS and Token generation (for testing)
- OpenAI Integration via Spring AI (2.0.0-M2)
- Spring Web (MVC) and WebFlux (Asynchronous WebClient)
- Persistence with MyBatis and jOOQ
- Configuration for Virtual Threads and graceful shutdown
- Comprehensive testing suite including Unit (test) and Integration Tests (test-it)

## Requirements

- Java 25 (JDK 25)
- Gradle 9.x+
- PostgreSQL (or compatible database)

## Project Structure

- `build.gradle`: plugins, dependencies and configuration definitions (includes jOOQ code generation task)
- `src/main/java/com/example/poc/Application.java`: main Spring Boot class
- `src/main/resources/application.yml`: Configuration for:
  - server
  - logging
  - security
  - AI
  - datasource + MyBatis + jOOQ
- `src/main/java/com/example/poc/controller/`:
    - `BlogController`: Main API for blog summarization.
    - `HealthController`: Application health status.
    - `JwksController`: Exposes JWK Set for local JWT verification.
    - `TokenController`: Utility to generate test tokens.

## Key Dependencies Included

- `org.springframework.boot:spring-boot-starter-web` & `webflux`
- `org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1`
- `org.springframework.boot:spring-boot-starter-jooq` (jOOQ 3.19.30)
- `org.springframework.boot:spring-boot-starter-oauth2-resource-server`
- `org.springframework.ai:spring-ai-starter-model-openai:2.0.0-M2`

## Logic Details: Blog Summarization

The project implements a specific logic for managing blog summaries, combining different persistence frameworks and AI:

### 1. Creation Process (MyBatis + OpenAI)

When a POST request is made to `/api/v1/blog/summarize`:

- **Persistence (MyBatis)**: The application uses **MyBatis** to insert the initial request records into the database.
  It handles the master record in `BLOG_REQUEST` and multiple entries in `BLOG_REQUEST_CONTENT` using specific mappers (`BlogRequestMapper`, `BlogRequestContentMapper`).
- **AI Processing (OpenAI)**: The `BlogAIServiceImpl` interacts with **OpenAI** (via Spring AI) to process the blog content. It uses a technical system prompt to generate a concise summary.
- **Completion (MyBatis)**: Once the summary is received, MyBatis is used again to update the original record with the generated text.

### 2. Reading Process (jOOQ)

When a GET request is made to `/api/v1/blog/summarize`:

- **Data Retrieval (jOOQ)**: The application utilizes **jOOQ**'s DSL to perform a type-safe query against the `BLOG_REQUEST` table.
- It fetches the `BLOG_REQUEST_ID` and `SUMMARIZE` fields and maps them directly into `BlogSummarizeRow` DTOs using jOOQ's `fetchInto` capabilities.

## Configuration

Fill in the necessary values in `application.yml`:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9025/.well-known/jwks.json
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: user
    password: secret
```

For jOOQ the generation configuration is provided in `build.gradle`. Ensure environment variables (`POSTGRES_URL`, etc.) are set or use defaults, then run:

```bash
./gradlew generateJooq
```

## Running the Application

```bash
./gradlew bootRun
```

## Running Tests

The project includes both unit and integration tests:

- **Unit Tests**: Located in `src/test`. Focus on individual component logic.
  ```bash
  ./gradlew test
  ```
- **Integration Tests (IT)**: Located in `src/test-it`. Verify full flows using an in-memory database (SQLite) and mocked external services.
  ```bash
  ./gradlew integrationTest
  ```
- **All Tests & Coverage**: Run all tests and generate a JaCoCo report.
  ```bash
  ./gradlew allTests jacocoTestReport
  ```

## API Endpoints

- `GET /health`: Check application status.
- `GET /.well-known/jwks.json`: Get the public keys for JWT verification.
- `POST /token/generate`: Generate a JWT for testing.
- `POST /api/v1/blog/summarize`: Create a new summary (by User in Session).
- `GET /api/v1/blog/summarize`: List existing summaries (filtered by User in Session).

#### Token generate example
```
curl --request POST \
  --url http://localhost:9025/token/generate \
  --header 'Content-Type: application/json' \
  --cookie JSESSIONID=568977AA683E996D7A539F7F9F38A8AB \
  --data '{"subject":"client1","scopes":"read"}'
```
#### Post Summarize example
```
curl --request POST \
  --url http://localhost:9025/api/v1/blog/summarize \
  --header 'Authorization: Bearer [TOKEN_RESPONSE_VALUE_FROM_GENERATE_ENDPOINT]' \
  --header 'Content-Type: application/json' \
  --data '[
  {
    "url": "https://gabogil.com/2026/02/16/the-state-of-rust-ecosystem-the-rustrover-blog/",
    "article": "In 2025, the Rust ecosystem has transitioned from a niche experimental language into a mature industry standard defined by professional adoption and rapid growth. While a significant portion of the community still explores Rust through hobby projects, over 26% of developers now utilize it in professional environments, reflecting a shift toward long-term stability. This momentum is fueled by a constant influx of newcomers—30% of whom started within the last month—and a notable migration of teams from legacy C and C++ projects who find that the learning curve is no longer the “vertical” obstacle it once was."
  },
  {
    "url": "https://gabogil.com/2026/01/31/mastering-focus-why-your-brain-hates-multitasking/",
    "article": "Neuroscience confirms the prefrontal cortex is a single threaded processor. Rubinstein, Meyer & Evans (2001) measured a 20% drop in task efficiency each time knowledge workers switch contexts. Csikszentmihalyis flow research (1990) shows deep work blocks of 60–90 minutes triple creative output compared to fragmented schedules."
  },
  {
    "url": "https://gabogil.com/2025/12/01/adapting-to-spring-boot-4-and-framework-7-0-a-new-enterprise-baseline/",
    "article": "In mid-November 2025, the Spring team delivered Framework 7.0 and Boot 4.0 as the next generation of its flagship platform. These releases pivot the ecosystem to require Java 25, integrate GraalVM native-image improvements, and overhaul core modules for reactive and cloud-native patterns. Over 30% of Fortune 500 back-end Java systems run on Spring; this transition is not incremental. Companies must balance the promised performance gains—benchmarks show up to 20% lower CPU use on virtual-thread workloads—against the cost of upgrading core libraries and retraining teams on new idioms."
  }
]'
```
#### Get Summarize example
```
curl --request GET \
  --url http://localhost:9025/api/v1/blog/summarize \
  --header 'Authorization: Bearer [TOKEN_RESPONSE_VALUE_FROM_GENERATE_ENDPOINT]' \
  --header 'Content-Type: application/json'
```

## Virtual Threads and Optimization

- Enabled with `spring.threads.virtual.enabled=true`.
- `spring.main.lazy-initialization=true` for faster startup in development.
- Graceful shutdown enabled for clean process termination.

## License

Refer to the `LICENSE` file.

## Contact
Blog: https://gabogil.com/blog/

Github: https://github.com/gabo-gil-playground

Linkedin: https://www.linkedin.com/in/gabogil/