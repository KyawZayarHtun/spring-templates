package com.example.util.payload.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginReq(
        @NotBlank(message = "Email is require!")
        String email,
        @NotBlank(message = "Password is require!")
        String password
) {
}
