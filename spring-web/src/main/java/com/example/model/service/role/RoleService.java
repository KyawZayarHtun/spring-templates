package com.example.model.service.role;

import org.springframework.security.core.Authentication;

public interface RoleService {
    String getCurrentUserRoleName();
    String getCurrentUserRoleName(Authentication auth);
}
