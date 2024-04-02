package com.example.util.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -1713052294630582240L;

    private final String message;

    protected BaseException(String message) {
        super();
        this.message = message;
    }
}
