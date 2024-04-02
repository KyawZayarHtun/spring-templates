package com.example.util.exception;

import java.io.Serial;

public class ExpiredRefreshTokenException extends BaseException{

    @Serial
    private static final long serialVersionUID = -2571086053053059033L;

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}
