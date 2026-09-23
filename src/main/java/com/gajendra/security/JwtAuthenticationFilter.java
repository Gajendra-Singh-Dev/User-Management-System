package com.gajendra.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gajendra.entity.User;
import com.gajendra.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ==========================================
        // GET AUTHORIZATION HEADER
        // ==========================================

        String authHeader =
                request.getHeader("Authorization");

        // Token nahi hai
        if (authHeader == null ||
            !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ==========================================
        // EXTRACT TOKEN
        // ==========================================

        String token =
                authHeader.substring(7).trim();

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            // ==========================================
            // TOKEN SE EMAIL NIKALO
            // ==========================================

            String email =
                    jwtService.extractUsername(token);

            if (email == null || email.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // ==========================================
            // DATABASE USER FIND
            // ==========================================

            User user =
                    userRepository
                    .findByEmail(email)
                    .orElse(null);

            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ==========================================
            // CHECK ACTIVE
            // ==========================================

            if (Boolean.FALSE.equals(user.getActive())) {
                filterChain.doFilter(request, response);
                return;
            }

            // ==========================================
            // AUTHENTICATION ALREADY SET?
            // ==========================================

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                // ==========================================
                // VALIDATE TOKEN
                // ==========================================

                if (jwtService.isTokenValid(token, user)) {

                    String role =
                            user.getRole().name();

                    // IMPORTANT:
                    // Spring Security role format:
                    // ROLE_ADMIN
                    // ROLE_STAFF
                    // ROLE_STUDENT

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                "ROLE_" + role
                            );

                    // ==========================================
                    // CREATE AUTHENTICATION
                    // ==========================================

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(authority)
                            );

                    // ==========================================
                    // SET SECURITY CONTEXT
                    // ==========================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "================================="
                    );

                    System.out.println(
                            "JWT Authentication SUCCESS"
                    );

                    System.out.println(
                            "User: " + user.getEmail()
                    );

                    System.out.println(
                            "Role: " + role
                    );

                    System.out.println(
                            "================================="
                    );
                }
            }

        } catch (Exception e) {

            SecurityContextHolder
                    .clearContext();

            System.out.println(
                "JWT validation failed: "
                + e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}