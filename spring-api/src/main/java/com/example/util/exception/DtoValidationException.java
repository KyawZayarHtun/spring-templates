package com.example.util.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DtoValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -386636535032112727L;

    private final Map<String, String> messages = new HashMap<>();

    public DtoValidationException(BindingResult result) {
        for (var fe : result.getFieldErrors()) {
            messages.put(fe.getField(), fe.getDefaultMessage());
        }
    }
}
