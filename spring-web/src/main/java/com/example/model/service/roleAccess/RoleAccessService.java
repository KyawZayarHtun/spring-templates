package com.example.model.service.roleAccess;

import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;

import java.util.List;
import java.util.Optional;

public interface RoleAccessService {

    List<RoleAccessDto> findRoleAccessByRole(String roleName);

    Optional<RoleAccessForm> findRoleAccessById(Long id);

    void manageRoleAccess(RoleAccessForm dto);

    boolean roleAccessNameExists(String roleAccessName);
}
