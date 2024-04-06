package com.example.util.payload.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record IdRequest(
        @NotNull(message = "Id can't be empty or null!")
        @Positive(message = "Id must be positive number")
        Long id
) {
}
