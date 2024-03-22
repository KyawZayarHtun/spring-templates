package com.example.config.security;

import com.example.model.repo.UserRepo;
import com.example.model.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo repo;
    private final UserService userService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.getUserDetailByEmail(email).map(user -> User
                .withUsername(email)
                .username(user.name())
                .authorities(user.role())
                .password(user.password())
                .accountLocked(user.isLocked())
                .disabled(!user.isActivated())
                .build()).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
