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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final CustomAuthorizationManager authorizationManager;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {

        http.authorizeHttpRequests(req -> {
            req.requestMatchers("/login", "/css/**", "/images/**", "/js/**").permitAll();
            req.anyRequest().access(authorizationManager);
        });

        http.formLogin(form -> {
            form.loginPage("/login");
            form.defaultSuccessUrl("/");
        });

        http.logout(Customizer.withDefaults());

        http.sessionManagement(session -> session.maximumSessions(1));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http,
                                                PasswordEncoder passwordEncoder) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider(passwordEncoder));
        return authenticationManagerBuilder.build();
    }

    @Bean
    AuthenticationProvider customAuthenticationProvider(PasswordEncoder passwordEncoder) {
        var provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

}
