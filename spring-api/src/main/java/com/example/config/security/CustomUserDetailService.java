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
        return userService.getUserDetailByEmail(email)
                .map(user -> User
                        .withUsername(email)
                        .username(user.email())
                        .authorities(user.role())
                        .password(user.password())
                        .accountLocked(user.isLocked())
                        .disabled(!user.isActivated())
                        .build()
                ).orElseThrow(() -> new UsernameNotFoundException("You don't have a account!"));
    }
}
