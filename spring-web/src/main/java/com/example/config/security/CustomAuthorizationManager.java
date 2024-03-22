package com.example.config.security;

import com.example.model.service.role.RoleService;
import com.example.model.service.roleAccess.RoleAccessService;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final RoleAccessService roleAccessService;
    private final RoleService roleService;

    @Transactional
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        var auth = authentication.get();
        var httpRequest = object.getRequest();
        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        if (auth instanceof AnonymousAuthenticationToken) {
            return new AuthorizationDecision(false);
        }

        if (auth.isAuthenticated()) {
            var hasRoleAccess =
                    roleAccessService.findRoleAccessByRole(roleService.getCurrentUserRoleName(auth))
                            .stream()
                            .filter(ra -> ra.url().equals(requestUri) && ra.requestMethod().name().equals(requestMethod))
                            .findAny();

            if (hasRoleAccess.isPresent())
                return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }
}
