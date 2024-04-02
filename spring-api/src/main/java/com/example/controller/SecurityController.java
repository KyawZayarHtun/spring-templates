package com.example.controller;

import com.example.config.security.jwt.JwtTokenProvider;
import com.example.model.service.user.UserService;
import com.example.util.exception.DtoValidationException;
import com.example.util.exception.ExpiredRefreshTokenException;
import com.example.util.exception.WrongRefreshTokenException;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.dto.security.LoginReq;
import com.example.util.payload.dto.security.LoginResult;
import com.example.util.payload.dto.security.RefreshTokenReq;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("login")
    public ApiResponse<LoginResult> signIn(@RequestBody @Valid LoginReq loginForm, BindingResult result) {
        if (result.hasErrors()) {
            throw new DtoValidationException(result);
        }
        var authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginForm.email(), loginForm.password()));
        var accessToken = jwtTokenProvider.generateToken(authentication, false);
        var refreshToken = jwtTokenProvider.generateToken(authentication, true);
        return ApiResponse.success(new LoginResult(accessToken, refreshToken));
    }

    @PostMapping("refreshToken")
    public ApiResponse<LoginResult> refreshToken(@RequestBody @Valid RefreshTokenReq refreshTokenReq,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            throw new DtoValidationException(result);
        }

        try {
            Authentication authentication = jwtTokenProvider.parse(refreshTokenReq.refreshToken());
            var email = authentication.getName();
            userService.getUserDetailByEmail(email)
                    .orElseThrow(() -> new WrongRefreshTokenException("Wrong Refresh Token"));

            if (authentication.isAuthenticated())
                SecurityContextHolder.getContext().setAuthentication(authentication);

            var accessToken = jwtTokenProvider.generateToken(authentication, false);
            var refreshToken = jwtTokenProvider.generateToken(authentication, true);
            return ApiResponse.success(new LoginResult(accessToken, refreshToken));
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException("Token Expired!");
        }
    }

}
