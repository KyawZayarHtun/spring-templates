package com.example.util.exception;

import org.springframework.lang.NonNull;

import java.io.Serial;

public class RoleNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -8768582939232035763L;

    public RoleNotFoundException() {
        super("User does not have roleAccess!");
    }
}
