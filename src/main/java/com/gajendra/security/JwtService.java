package com.gajendra.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gajendra.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ==========================================
    // GENERATE JWT TOKEN
    // ==========================================

    public String generateToken(User user) {

        return Jwts.builder()

                .subject(user.getEmail())

                .claim(
                    "userId",
                    user.getId()
                )

                .claim(
                    "role",
                    user.getRole().name()
                )

                .issuedAt(new Date())

                .expiration(
                    new Date(
                        System.currentTimeMillis()
                        + jwtExpiration
                    )
                )

                .signWith(getSigningKey())

                .compact();
    }

    // ==========================================
    // EXTRACT USERNAME / EMAIL
    // ==========================================

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // ==========================================
    // VALIDATE TOKEN
    // ==========================================

    public boolean isTokenValid(
            String token,
            User user) {

        String email =
                extractUsername(token);

        return email.equals(user.getEmail())
                && !isTokenExpired(token);
    }

    // ==========================================
    // CHECK TOKEN EXPIRATION
    // ==========================================

    private boolean isTokenExpired(
            String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ==========================================
    // EXTRACT ALL CLAIMS
    // ==========================================

    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    // ==========================================
    // SECRET KEY
    // ==========================================

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                secretKey.getBytes(
                    StandardCharsets.UTF_8
                );

        return Keys.hmacShaKeyFor(keyBytes);
    }
}