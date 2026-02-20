package com.example.poc.controller;

import com.example.poc.constant.Constants;
import com.example.poc.jwt.JwkService;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Public endpoint that returns the JWKS (JSON Web Key Set).
 * <p>
 * OIDC clients / resource servers use this endpoint to obtain the public key(s) used to verify tokens.
 */
@RestController
public final class JwksController {

    private final JwkService jwkService;

    public JwksController(JwkService jwkService) {
        this.jwkService = jwkService;
    }

    @GetMapping(value = Constants.API_WELL_KNOWN_JSON_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> jwks() {
        JWKSet jwkSet = jwkService.getJwkSet();
        return ResponseEntity.ok(jwkSet.toJSONObject());
    }
}