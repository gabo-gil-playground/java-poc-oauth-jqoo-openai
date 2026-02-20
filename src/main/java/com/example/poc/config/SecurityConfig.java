package com.example.poc.config;

import com.example.poc.constant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.poc.constant.Constants.API_WELL_KNOWN_JSON_PATH;

/**
 * Security configuration for the P.o.C. application (just for non-prod environment):
 * <p>
 * - Uses OAuth2 Resource Server (JWT) configuration.
 * - Disables CSRF for the POC using the lambda-style API.
 * - Maps 'scope' claim into authorities prefixed with 'SCOPE_'.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure the security filter chain.
     * Note: lambda-style configuration is used to align with Spring Security 7 API expectations.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for this POC (evaluate carefully for production)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(API_WELL_KNOWN_JSON_PATH, Constants.API_TOKEN_GENERATE_PATH, Constants.API_HEALTH_PATH) // Authorization rules
                .permitAll() // Public endpoints: JWKS and a dev token generator (if present)
                .requestMatchers(Constants.API_MAIN_PATH)
                .hasAuthority(Constants.OAUTH_SCOPE_READ) // Endpoint that requires "read" scope -> mapped to authority "SCOPE_read"
                .anyRequest()
                .authenticated()) // All other requests require authentication
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())) // Configure resource server to use JWTs and supply a converter for authorities
            );

        return http.build();
    }

    /**
     * JwtDecoder built from the configured jwk-set-uri.
     * NimbusJwtDecoder handles fetching and caching the JWKS.
     *
     * @param jwkSetUri the JWKS URI configured in application properties
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setJwtValidator(JwtValidators.createDefault()); // default validators (exp, nbf, iat checks) are applied.

        return jwtDecoder;
    }

    /**
     * Converter that maps JWT 'scope' claim into GrantedAuthorities with prefix "SCOPE_".
     * <p>
     * Spring Security will then allow checks like hasAuthority("SCOPE_read").
     */
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Ensure authorities are prefixed as SCOPE_ (Spring's default is "SCOPE_" but explicitly set here)
        grantedAuthoritiesConverter.setAuthorityPrefix(Constants.OAUTH_SCOPE_PREFIX);
        // Use 'scope' claim name (some IdPs use 'scp' or 'scope' — pick the one you produce)
        grantedAuthoritiesConverter.setAuthoritiesClaimName(Constants.OAUTH_SCOPE_CLAIM_NAME);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}