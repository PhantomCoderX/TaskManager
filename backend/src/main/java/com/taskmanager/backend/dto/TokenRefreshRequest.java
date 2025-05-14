package com.taskmanager.backend.dto;

public record TokenRefreshRequest(
        String refreshToken
) {}
