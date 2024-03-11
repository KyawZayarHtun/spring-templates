package com.example.util.payload;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        LocalDateTime issueAt,
        T Payload
) {

    @ResponseStatus(HttpStatus.OK)
    public static <T> ApiResponse<T> success(T payload) {
        return new ApiResponse<>(LocalDateTime.now(), payload);
    }

    public static <T> ApiResponse<T> failure(T payload) {
        return new ApiResponse<>(LocalDateTime.now(), payload);
    }

}
