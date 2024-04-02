package com.example.util.exception;

import java.io.Serial;

public class SomethingWrongException extends BaseException{


    @Serial
    private static final long serialVersionUID = 2992815398793229254L;

    public SomethingWrongException(String message) {
        super(message);
    }
}
