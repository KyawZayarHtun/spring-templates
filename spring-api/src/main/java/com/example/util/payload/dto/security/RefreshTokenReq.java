package com.example.util.payload.dto.security;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenReq(
        @NotBlank(message = "Refresh token cannot be empty!")
        String refreshToken
) {
}
