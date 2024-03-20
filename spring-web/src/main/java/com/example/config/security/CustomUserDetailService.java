package com.example.config.security;

import com.example.model.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.getUserDetailForSecurityByEmail(email)
                .map(user -> User
                        .withUsername(email)
                        .username(user.name())
                        .password(user.password())
                        .authorities(user.role())
                        .accountLocked(user.isLocked())
                        .disabled(!user.isActivated())
                        .build())
                .orElseThrow();
    }
}
