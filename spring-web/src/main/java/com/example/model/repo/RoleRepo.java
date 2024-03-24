package com.example.model.repo;

import com.example.model.entity.Role;
import com.example.model.repo.base.BaseRepository;

public interface RoleRepo extends BaseRepository<Role, Long> {

    boolean existsByName(String roleName);

}
