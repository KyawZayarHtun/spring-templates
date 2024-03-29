package com.example.controller;

import com.example.model.service.role.RoleService;
import com.example.model.service.roleAccess.RoleAccessService;
import com.example.model.service.user.UserService;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class LayoutControllerAdvice {

    private final RoleService roleService;
    private final RoleAccessService roleAccessService;
    private final UserService userService;

    @ModelAttribute("accessUrls")
    public List<RoleAccessDto> accessUrls(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return new ArrayList<>();
        String role = roleService.getCurrentUserRoleName(auth);
        return roleAccessService.findRoleAccessByRoleName(role);
    }

    @ModelAttribute("loginUsername")
    public String username(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return "";
       return userService.getLoginUsername();
    }

}
