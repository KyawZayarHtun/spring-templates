package com.example.util.payload.dto.role;

import com.example.util.constant.RequestMethod;

public record RoleAccessDto(
        String url,
        RequestMethod requestMethod
) {
}
