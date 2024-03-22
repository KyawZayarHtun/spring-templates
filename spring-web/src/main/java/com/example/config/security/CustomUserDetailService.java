package com.example.config.security;

import com.example.model.repo.UserRepo;
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

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email).map(user -> User
                .withUsername(email)
                .username(user.getName())
                .authorities(user.getRole().getName())
                .password(user.getPassword())
                .accountLocked(user.getLocked())
                .disabled(!user.getActivated())
                .build()).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
