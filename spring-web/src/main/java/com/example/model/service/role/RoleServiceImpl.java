package com.example.model.service.role;

import com.example.util.exception.RoleNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public String getCurrentUserRoleName() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return getRoleNameFromAuthentication(auth);
    }

    @Override
    public String getCurrentUserRoleName(Authentication auth) {
        return getRoleNameFromAuthentication(auth);
    }

    private String getRoleNameFromAuthentication(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
                .orElseThrow(RoleNotFoundException::new);
    }
}
