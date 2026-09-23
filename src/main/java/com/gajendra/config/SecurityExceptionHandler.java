package com.gajendra.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class SecurityExceptionHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // ==========================================
    // 401 UNAUTHORIZED
    // ==========================================

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                401,
                "Unauthorized",
                "Authentication required",
                request.getRequestURI()
        );

        objectMapper.writeValue(
                response.getWriter(),
                errorResponse
        );
    }

    // ==========================================
    // 403 FORBIDDEN
    // ==========================================

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                403,
                "Forbidden",
                "You do not have permission to access this resource",
                request.getRequestURI()
        );

        objectMapper.writeValue(
                response.getWriter(),
                errorResponse
        );
    }

    // ==========================================
    // ERROR RESPONSE
    // ==========================================

    public static class ErrorResponse {

        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(
                int status,
                String error,
                String message,
                String path) {

            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }
    }
}