package com.example.util.exception.handler;

import com.example.util.exception.DtoValidationException;
import com.example.util.exception.ExpiredRefreshTokenException;
import com.example.util.exception.SomethingWrongException;
import com.example.util.exception.WrongRefreshTokenException;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.dto.error.ErrorRes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> validationError(DtoValidationException e) {
        return ApiResponse.validationError(e.getMessages());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> httpConvertError(HttpMessageNotReadableException e) {
        return ApiResponse.badRequest(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler
    public ApiResponse<ErrorRes> usernameNotFoundError(AuthenticationException e) {
        if (e instanceof UsernameNotFoundException  )
            return ApiResponse.authenticationError(new ErrorRes("Login Fail! Something was wrong!"));
        if (e instanceof LockedException)
            return ApiResponse.authenticationError(new ErrorRes("Please contact to admin for you account! Your Account is locked by admin."));
        if (e instanceof DisabledException)
            return ApiResponse.authenticationError(new ErrorRes("Please contact to admin for you account! Your Account is inactive by admin."));
        return ApiResponse.authenticationError(new ErrorRes(e.getMessage()));
    }

    // for access denied exception
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<ErrorRes> accessDeniedError() {
        return ApiResponse.accessDeniedError(new ErrorRes("You Don't have access to reach this page! If you have any query, kindly ask admin."));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> expiredJwtError(JwtException e) {
        if (e instanceof ExpiredJwtException)
            return ApiResponse.jwtExpiredError(new ErrorRes("Your Jwt token is Expired!"));
        if (e instanceof SignatureException)
            return ApiResponse.wrongJwtSignature(new ErrorRes("Wrong Jwt token!"));
        return ApiResponse.jwtError(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> expiredJwtError(WrongRefreshTokenException e) {
        return ApiResponse.wrongRefreshTokenError(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> expiredJwtError(ExpiredRefreshTokenException e) {
        return ApiResponse.refreshTokenExpiredError(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> someThingWrongError(SomethingWrongException e) {
        return ApiResponse.badRequest(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorRes> badRequest(BadRequestException e) {
        return ApiResponse.badRequest(new ErrorRes(e.getMessage()));
    }

}
