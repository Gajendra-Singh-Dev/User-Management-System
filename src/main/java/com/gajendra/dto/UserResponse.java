package com.gajendra.dto;

public class UserResponse {

    private final String token;
    private final String tokenType;
    private final String message;

    public UserResponse (String token, String tokenType, String message) {
        this.token = token;
        this.tokenType = tokenType;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getMessage() {
        return message;
    }
}



