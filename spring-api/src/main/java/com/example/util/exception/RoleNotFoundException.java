package com.example.util.exception;

import java.io.Serial;

public class RoleNotFoundException extends BaseException{

    @Serial
    private static final long serialVersionUID = -8768582939232035763L;

    public RoleNotFoundException() {
        super("User does not have roleAccess!");
    }
}
