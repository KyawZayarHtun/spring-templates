package com.example.util.payload.dto.roleAccess;

import com.example.util.constant.CrudOperation;
import com.example.util.constant.RequestMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RoleAccessCreateForm(
        @NotEmpty(message = "Role access name can't be empty or null!")
        String name,

        @NotEmpty(message = "Role access url can't be empty or null!")
        String url,

        @NotNull(message = "Request Method can't be empty or null!")
        RequestMethod requestMethod,

        @NotNull(message = "Request Operation can't be empty or null!")
        CrudOperation requestOperation,

        String description
) {
}
