package com.example.util.validation.roleNameUnique;

import com.example.model.service.role.RoleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueRoleNameConstraint implements ConstraintValidator<UniqueRoleName, String> {

    private final RoleService roleService;

    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext constraintValidatorContext) {
        return roleService.roleNameExist(roleName);
    }

}
