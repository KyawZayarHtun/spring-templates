package com.example.util.validation.roleAccessNameUnique;

import com.example.util.validation.roleNameUnique.UniqueRoleNameConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, PARAMETER })
@Constraint(validatedBy = UniqueRoleAccessNameConstraint.class)
public @interface UniqueRoleAccessName {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
