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
    // Generate JWT Token
    // ==========================================

    public String generateToken(User user) {

        return Jwts.builder()

                // User email will become JWT subject
                .subject(user.getEmail())

                // User ID
                .claim("userId", user.getId())

                // User Role
                .claim("role", user.getRole().name())

                // Token created time
                .issuedAt(new Date())

                // Token expiry time
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )

                // Sign token
                .signWith(getSigningKey())

                // Generate final token
                .compact();
    }

    // ==========================================
    // Extract Email from Token
    // ==========================================

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // ==========================================
    // Check Token Valid
    // ==========================================

    public boolean isTokenValid(
            String token,
            User user) {

        String email = extractUsername(token);

        return email.equals(user.getEmail())
                && !isTokenExpired(token);
    }

    // ==========================================
    // Check Token Expiry
    // ==========================================

    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ==========================================
    // Extract All Claims
    // ==========================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    // ==========================================
    // JWT Secret Key
    // ==========================================

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}