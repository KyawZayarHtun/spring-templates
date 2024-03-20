package com.example.config.security;

import com.example.model.service.role.RoleService;
import com.example.model.service.roleAccess.RoleAccessService;
import com.example.model.service.user.UserService;
import com.example.util.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final RoleService roleService;
    private final RoleAccessService roleAccessService;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        var auth = authentication.get();
        var httpRequest = object.getRequest();
        var httpRequestMethod = httpRequest.getMethod();
        var httpRequestUri = httpRequest.getRequestURI();

        if (auth instanceof AnonymousAuthenticationToken)
            return new AuthorizationDecision(false);

        if (auth.isAuthenticated()) {

            var hasRoleAccess =
                    roleAccessService.findRoleAccessByRole(roleService.getCurrentUserRoleName(auth))
                    .stream()
                    .filter(ra -> ra.url().equals(httpRequestUri) && ra.requestMethod().name().equals(httpRequestMethod))
                    .findAny();

            if (hasRoleAccess.isPresent())
                return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }
}
