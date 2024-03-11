package com.example.util.exception.handler;

import com.example.util.exception.ValidationException;
import com.example.util.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> validationError(ValidationException e) {
        return ApiResponse.failure(e.getMessages());
    }

    // TODO: Capture Access Denied Exception


}
