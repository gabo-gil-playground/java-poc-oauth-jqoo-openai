package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.jwt.JwkService;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * JSON Web Key Set controller
 * <p>
 * IMPORTANT: provided just for local testing and POC purposes only. For production, tokens should be issued by a
 * proper Authorization Server.
 */
@Profile("!uat & !stage & !prod")
@RestController
public final class JwksController {

    private final JwkService jwkService;

    /**
     * Constructor
     *
     * @param jwkService the {@link JwkService} JSON Web Key Set service
     */
    public JwksController(final JwkService jwkService) {
        this.jwkService = jwkService;
    }

    /**
     * Returns the JWKS (JSON Web Key Set).
     * OIDC clients / resource servers use this endpoint to obtain the public key(s) used to verify tokens.
     *
     * @return {@link ResponseEntity<Map<String, Object>>}
     */
    @GetMapping(value = Constants.API_WELL_KNOWN_JSON_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getJsonWebKeySet() {
        JWKSet jwkSet = jwkService.getJwkSet();
        return ResponseEntity.ok(jwkSet.toJSONObject());
    }
}