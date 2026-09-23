package com.gajendra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gajendra.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            SecurityExceptionHandler securityExceptionHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.securityExceptionHandler = securityExceptionHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // ==========================================
            // 401 / 403 HANDLING
            // ==========================================

            .exceptionHandling(exception -> exception

                .authenticationEntryPoint(
                    securityExceptionHandler
                )

                .accessDeniedHandler(
                    securityExceptionHandler
                )
            )

            // ==========================================
            // AUTHORIZATION
            // ==========================================

            .authorizeHttpRequests(auth -> auth

                // PUBLIC
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // LOGGED-IN USER
                .requestMatchers("/api/users/me")
                .hasAnyRole(
                    "ADMIN",
                    "STAFF",
                    "STUDENT"
                )

                // ADMIN ONLY
                .requestMatchers("/api/users/**")
                .hasRole("ADMIN")

                // ADMIN + STAFF
                .requestMatchers("/api/enquiries/**")
                .hasAnyRole("ADMIN", "STAFF")

                .requestMatchers("/api/courses/**")
                .hasAnyRole("ADMIN", "STAFF")

                .requestMatchers("/api/batches/**")
                .hasAnyRole("ADMIN", "STAFF")

                .requestMatchers("/error")
                .permitAll()

                .anyRequest()
                .authenticated()
            )

            // ==========================================
            // JWT FILTER
            // ==========================================

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}