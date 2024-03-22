package com.example.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {

        // Authorization Configuration
        http.authorizeHttpRequests(req -> {
            req.requestMatchers("/login", "/css/**", "/images/**", "/js/**").permitAll();
            req.anyRequest().access(authorizationManager);
        });

        // Form Login Configuration
        http.formLogin(form -> {
            form.loginPage("/login");
            form.defaultSuccessUrl("/");
            form.usernameParameter("email");
        });

        // Logout Configuration
        http.logout(Customizer.withDefaults());

        // Session Configuration
        http.sessionManagement(session -> session.maximumSessions(2));

        return http.build();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean
    AuthenticationManager configure(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {

        var builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(getCustomProvider(passwordEncoder));
        return builder.build();
    }

    @Bean
    AuthenticationProvider getCustomProvider(PasswordEncoder passwordEncoder) {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

}
