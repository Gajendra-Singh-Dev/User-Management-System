package com.gajendra.security;


import java.io.IOException;

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
        // 1. Authorization Header read karo
        // ==========================================

        String authHeader =
                request.getHeader("Authorization");

        // Agar Authorization header nahi hai
        // ya Bearer se start nahi ho raha
        // to request ko aage bhej do

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ==========================================
        // 2. "Bearer " ke baad JWT token nikalo
        // ==========================================

        String token =
                authHeader.substring(7);

        try {

            // ==========================================
            // 3. JWT se email nikalo
            // ==========================================

            String email =
                    jwtService.extractUsername(token);

            // ==========================================
            // 4. Database se user find karo
            // ==========================================

            User user =
                    userRepository.findByEmail(email)
                            .orElse(null);

            // ==========================================
            // 5. User mila aur authentication nahi hai
            // ==========================================

            if (user != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // ==========================================
                // 6. JWT valid hai ya nahi
                // ==========================================

                if (jwtService.isTokenValid(token, user)) {

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                    "ROLE_" +
                                    user.getRole().name()
                            );

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    java.util.List.of(authority)
                            );

                    // ==========================================
                    // 7. SecurityContext me user set karo
                    // ==========================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            // Invalid / expired JWT
            // authentication set nahi hogi

            System.out.println(
                    "JWT validation failed: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // 8. Request ko next filter/controller par bhejo
        // ==========================================

        filterChain.doFilter(request, response);
    }
}