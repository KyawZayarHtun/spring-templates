package com.example.util.payload.dto.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoleUpdateForm(
        @NotNull(message = "Role id can't be empty or null!")
        @Positive(message = "Id must be positive number")
        Long id,
        @NotEmpty(message = "Role name can't be empty or null!")
        String name
) {
}
