package com.example.poc.jwt;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Service that generates an ephemeral RSA keypair at startup and exposes a JWKSet object.
 * <p>
 * This is intentionally simple for a POC: keys are generated in-memory and remain valid
 * while the JVM runs. For production, use a protected keystore or external IdP.
 */
@Service
@Slf4j
public final class JwkService {
    private RSAKey rsaJwk;
    private JWKSet jwkSet;

    @PostConstruct
    public void init() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048); // Generate RSA key pair (2048 bits)
        KeyPair kp = kpg.generateKeyPair();

        String kid = UUID.randomUUID().toString();

        this.rsaJwk = new RSAKey.Builder((RSAPublicKey) kp.getPublic())
            .privateKey(kp.getPrivate())
            .keyID(kid)
            .build();

        this.jwkSet = new JWKSet(this.rsaJwk.toPublicJWK());

        log.info("Generated ephemeral RSA keypair with kid={}", kid);
    }

    /**
     * Return the public JWKSet (used by /.well-known/jwks.json).
     */
    public JWKSet getJwkSet() {
        return jwkSet;
    }

    /**
     * Returns the private RSAKey for signing tokens (POC/test use only).
     */
    public RSAKey getRsaJwk() {
        return rsaJwk;
    }
}
