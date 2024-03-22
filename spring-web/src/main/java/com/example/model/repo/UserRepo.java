package com.example.model.repo;

import com.example.model.entity.User;
import com.example.model.repo.base.BaseRepository;

import java.util.Optional;

public interface UserRepo extends BaseRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
