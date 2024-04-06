package com.example.util.exception.handler;

import com.example.util.exception.DtoValidationException;
import org.apache.coyote.BadRequestException;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public class BindingResultHandler {

    public static void checkBindingResultError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DtoValidationException(bindingResult);
        }
    }

}
