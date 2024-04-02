package com.example.util.exception;

import java.io.Serial;

public class WrongRefreshTokenException extends BaseException{

    @Serial
    private static final long serialVersionUID = -2571086053053059033L;

    public WrongRefreshTokenException(String message) {
        super(message);
    }
}
