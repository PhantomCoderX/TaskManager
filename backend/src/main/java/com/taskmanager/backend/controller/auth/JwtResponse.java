package com.taskmanager.backend.controller.auth;

public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
    private static final String TOKEN_TYPE = "Bearer";
    private final Long id;
    private final String username;
    private final String email;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
