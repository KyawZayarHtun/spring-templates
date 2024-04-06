package com.example.util.payload.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RoleCreateForm(
        @NotEmpty(message = "Role name can't be empty or null!")
        String name
) {
}
