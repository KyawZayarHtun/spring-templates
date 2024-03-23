package com.example.util.exception;

import java.io.Serial;

public class LoginUserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3247991771668304807L;

    public LoginUserNotFoundException(String message) {
        super(message);
    }
}
