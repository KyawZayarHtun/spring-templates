package com.example.util.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> messages = new HashMap<>();

    public ValidationException(BindingResult bindingResult) {
        bindingResult.getFieldErrors()
                .forEach(fe -> messages.put(fe.getField(), fe.getDefaultMessage()));
    }

}
