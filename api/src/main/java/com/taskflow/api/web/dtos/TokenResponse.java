package com.taskflow.api.web.dtos;

public record TokenResponse(
        String token,
        String type
) {}