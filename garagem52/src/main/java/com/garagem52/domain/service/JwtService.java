package com.garagem52.domain.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * DOMAIN — JWT SERVICE
 * Lógica de geração e validação de tokens JWT pertence ao domínio:
 * é uma regra de negócio de autenticação, não um detalhe de infraestrutura.
 * JWT (JSON Web Token) — autenticação stateless:
 *   Estrutura: header.payload.signature (Base64 + HMAC-SHA256)
 *   Payload (claims): subject (email), role, iat (emitido), exp (expira)
 *   O servidor NÃO guarda sessão — valida o token pela assinatura a cada requisição.
 */
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Gera um token JWT assinado com HMAC-SHA256.
     * O token carrega: subject (email), role e tempo de expiração.
     */
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /** Extrai o e-mail (subject) do token */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /** Verifica assinatura e expiração */
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                java.util.Base64.getEncoder().encodeToString(secret.getBytes())
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
