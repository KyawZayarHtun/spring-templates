package com.example.util.validation.roleAccessNameUnique;

import com.example.model.service.roleAccess.RoleAccessService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueRoleAccessNameConstraint implements ConstraintValidator<UniqueRoleAccessName, String> {

    private final RoleAccessService roleAccessService;

    @Override
    public boolean isValid(String roleAccessName, ConstraintValidatorContext constraintValidatorContext) {
        return roleAccessService.roleAccessNameExists(roleAccessName);
    }

}
