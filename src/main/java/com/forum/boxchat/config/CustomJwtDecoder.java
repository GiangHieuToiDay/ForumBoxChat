package com.forum.boxchat.config;

import com.forum.boxchat.service.RefreshTokenService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerkey}")
    private String signerKey;

    private final RefreshTokenService refreshTokenService;
    private NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecoder(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostConstruct
    public void init() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        this.nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            Jwt jwt = nimbusJwtDecoder.decode(token);

            if (refreshTokenService.isAccessTokenBlacklisted(jwt.getId())) {
                throw new JwtException("Token has been revoked");
            }

            return jwt;
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }
}
