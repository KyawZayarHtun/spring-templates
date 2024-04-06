package com.example.util.exception.handler;

import com.example.util.exception.DtoValidationException;
import com.example.util.exception.ExpiredRefreshTokenException;
import com.example.util.exception.SomethingWrongException;
import com.example.util.exception.WrongRefreshTokenException;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.MessageRes;
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
import org.springframework.web.bind.MissingServletRequestParameterException;
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
    public ApiResponse<MessageRes> httpConvertError(HttpMessageNotReadableException e) {
        return ApiResponse.badRequest(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> missingParamError(MissingServletRequestParameterException e) {
        return ApiResponse.badRequest(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    public ApiResponse<MessageRes> usernameNotFoundError(AuthenticationException e) {
        if (e instanceof UsernameNotFoundException  )
            return ApiResponse.authenticationError(new MessageRes("Login Fail! Something was wrong!"));
        if (e instanceof LockedException)
            return ApiResponse.authenticationError(new MessageRes("Please contact to admin for you account! Your Account is locked by admin."));
        if (e instanceof DisabledException)
            return ApiResponse.authenticationError(new MessageRes("Please contact to admin for you account! Your Account is inactive by admin."));
        return ApiResponse.authenticationError(new MessageRes(e.getMessage()));
    }

    // for access denied exception
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<MessageRes> accessDeniedError() {
        return ApiResponse.accessDeniedError(new MessageRes("You Don't have access to reach this page! If you have any query, kindly ask admin."));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> expiredJwtError(JwtException e) {
        if (e instanceof ExpiredJwtException)
            return ApiResponse.jwtExpiredError(new MessageRes("Your Jwt token is Expired!"));
        if (e instanceof SignatureException)
            return ApiResponse.wrongJwtSignature(new MessageRes("Wrong Jwt token!"));
        return ApiResponse.jwtError(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> expiredJwtError(WrongRefreshTokenException e) {
        return ApiResponse.wrongRefreshTokenError(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> expiredJwtError(ExpiredRefreshTokenException e) {
        return ApiResponse.refreshTokenExpiredError(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> someThingWrongError(SomethingWrongException e) {
        return ApiResponse.badRequest(new MessageRes(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<MessageRes> badRequest(BadRequestException e) {
        return ApiResponse.badRequest(new MessageRes(e.getMessage()));
    }

}
