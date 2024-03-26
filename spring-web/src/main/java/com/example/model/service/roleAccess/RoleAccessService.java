package com.example.model.service.roleAccess;

import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface RoleAccessService {

    List<RoleAccessDto> findRoleAccessByRole(String roleName);

    Optional<RoleAccessForm> findRoleAccessById(Long id);

    Optional<RoleAccessForm> findRoleAccessByName(String roleAccessName);

    void manageRoleAccess(RoleAccessForm dto);

    boolean roleAccessNameExists(@Nullable Long id, String roleAccessName);
}
