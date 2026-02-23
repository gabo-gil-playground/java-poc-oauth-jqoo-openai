# java-poc-oauth-jooq-openai

Basic Java project skeleton with Gradle, Java 25, and Spring Boot 4, pre-configured with:

- Spring Web (MVC) and WebFlux (Asynchronous WebClient)
- Persistence with JPA, MyBatis, and jOOQ
- OAuth2 Security: Resource Server with local JWKS and Token generation (for testing)
- OpenAI Integration via Spring AI
- Lombok support
- Configuration for Virtual Threads, lazy initialization, and graceful shutdown
- Testing dependencies for unit and integration tests

## Requirements

- Java 25 (JDK 25)
- Gradle 9.x+
- PostgreSQL (or compatible database)

## Project Structure

- `build.gradle`: plugins, dependencies, and configuration definitions (includes jOOQ code generation task)
- `settings.gradle`: project name
- `src/main/java/com/example/poc/Application.java`: main bootstrap class
- `src/main/resources/application.yml`: Configuration for server, logging, security, AI, datasource, MyBatis, and jOOQ
- `src/main/java/com/example/poc/controller/`: New controllers added:
    - `BlogController`: Main API for blog summarization.
    - `HealthController`: Application health status.
    - `JwksController`: Exposes JWK Set for local JWT verification.
    - `TokenController`: Utility to generate test tokens.

## Key Dependencies Included

- `org.springframework.boot:spring-boot-starter-web` & `webflux`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1`
- `org.springframework.boot:spring-boot-starter-jooq` (jOOQ 3.19.30)
- `org.springframework.boot:spring-boot-starter-oauth2-resource-server`
- `org.springframework.ai:spring-ai-starter-model-openai:2.0.0-M2`
- `org.projectlombok:lombok`

## Logic Details: Blog Summarization

The project implements a specific logic for managing blog summaries, combining different persistence frameworks and AI:

### 1. Creation Process (MyBatis + OpenAI)
When a POST request is made to `/api/v1/blog/summarize`:
- **Persistence (MyBatis)**: The application uses **MyBatis** to insert the initial request records into the database. It handles the master record in `BLOG_REQUEST` and multiple entries in `BLOG_REQUEST_CONTENT` using specific mappers (`BlogRequestMapper`, `BlogRequestContentMapper`).
- **AI Processing (OpenAI)**: The `BlogAIServiceImpl` interacts with **OpenAI** (via Spring AI) to process the blog content. It uses a technical system prompt to generate a concise 2-3 line summary.
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

For jOOQ, a generation configuration is provided in `build.gradle`. Ensure environment variables (`POSTGRES_URL`, etc.) are set or use defaults, then run:

```bash
./gradlew generateJooq
```

## Running the Application

```bash
./gradlew bootRun
```

## API Endpoints

- `GET /health`: Check application status.
- `GET /.well-known/jwks.json`: Get the public keys for JWT verification.
- `POST /token/generate`: Generate a JWT for testing.
- `POST /api/v1/blog/summarize`: Create a new summary.
- `GET /api/v1/blog/summarize`: List existing summaries.

## Virtual Threads and Optimization

- Enabled with `spring.threads.virtual.enabled=true`.
- `spring.main.lazy-initialization=true` for faster startup in development.
- Graceful shutdown enabled for clean process termination.

## License

Refer to the `LICENSE` file.
