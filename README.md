# java-poc-oauth-jooq-openai

Basic Java project skeleton with Gradle, Java 25, and Spring Boot 4, pre-configured with:

- Spring Web (MVC) and WebFlux (Asynchronous WebClient)
- Persistence with JPA, MyBatis, and jOOQ
- OAuth2 Security: Resource Server and Authorization Server
- OpenAI Integration via Spring AI
- Lombok support
- Configuration for Virtual Threads, lazy initialization, and minimal memory/CPU adjustments
- Testing dependencies for unit and integration tests

## Requirements

- Java 25 (JDK 25)
- Gradle (it is recommended to use the wrapper once added, or your local installation)

## Project Structure

- `build.gradle`: plugins, dependencies, and configuration definitions (includes basic jOOQ as a placeholder)
- `settings.gradle`: project name
- `gradle.properties`: compilation and cache settings
- `src/main/java/com/example/poc/Application.java`: main bootstrap class
- `src/main/resources/application.yml`: Virtual Threads, lazy-init, logging, and management settings
- `src/test/java/com/example/poc/ApplicationTests.java`: context load test

## Key Dependencies Included

- `org.springframework.boot:spring-boot-starter`
- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-webflux`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.mybatis.spring.boot:mybatis-spring-boot-starter`
- `org.jooq:jooq`
- `org.springframework.boot:spring-boot-starter-oauth2-resource-server`
- `org.springframework.security:spring-security-oauth2-authorization-server`
- `org.springframework.ai:spring-ai-openai-spring-boot-starter`
- `org.projectlombok:lombok`
- `org.springframework.boot:spring-boot-starter-test`, `spring-security-test`

Important Note: Spring Boot 4 and some associated starters may be evolving. If you encounter version incompatibilities,
adjust the Spring Boot plugin version or temporarily align with a stable version (e.g., Boot 3.3.x).

## Configuration

Fill in the necessary values in `application.yml` when using each module:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://issuer.example.com
  datasource:
    url: jdbc:postgresql://localhost:5432/app
    username: app
    password: app
# OpenAI Key
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:}
```

For jOOQ, a minimal generation configuration is provided in `build.gradle` (using PostgreSQL as an example). Adjust
`jdbc` and `database` according to your database and then run:

```bash
./gradlew generateJooq
```

## Running the Application

```bash
./gradlew bootRun
```

## Running Tests

```bash
./gradlew test
```

## Virtual Threads and Optimization

- Enabled with `spring.threads.virtual.enabled=true` in `application.yml`.
- `spring.main.lazy-initialization=true` reduces the startup footprint.
- Logging and management with conservative values to minimize default costs.

## Web Stack Selection

This skeleton includes both MVC and WebFlux for flexibility (traditional Servlet APIs or reactive APIs with
`WebClient`). You can choose one by removing the unused starter.

## License

Refer to the `LICENSE` file.
