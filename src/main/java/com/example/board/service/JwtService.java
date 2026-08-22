package com.example.board.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "my-secret-key-for-spring-mini-project-jwt-2026";

    //private static final long EXPIRATION_TIME = 1000L * 60 * 60;

    private static final long accessTokenExpiration = 1 * 60 * 1000L;
    private static final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L;

    private final SecretKey key;

    public JwtService() {
        this.key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String createToken(String username, String role) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + accessTokenExpiration
        );

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {

        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(
                        new Date(now.getTime() + refreshTokenExpiration)
                )
                .signWith(key)
                .compact();
    }


    public String getUsername(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshToken(String token) {

        Claims claims = extractAllClaims(token);

        return "refresh".equals(
                claims.get("type", String.class)
        );
    }

    public boolean validateRefreshToken(String token) {

        try {
            Claims claims = extractAllClaims(token);

            return "refresh".equals(
                    claims.get("type", String.class)
            );

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}