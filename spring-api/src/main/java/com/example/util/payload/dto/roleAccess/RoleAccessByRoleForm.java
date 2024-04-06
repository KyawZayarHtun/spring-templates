package com.example.util.payload.dto.roleAccess;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record RoleAccessByRoleForm(

        @NotNull(message = "Role id can't be empty or null!")
        @Positive(message = "Id must be positive number")
        Long roleId,
        @NotNull(message = "Role Access Id can't be empty")
        List<Long> roleAccessIdList
) {

}
