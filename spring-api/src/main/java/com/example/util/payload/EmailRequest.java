package com.example.util.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmailRequest(
        @NotEmpty(message = "Email can't be empty or null!")
        String email
) {
}
