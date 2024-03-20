package com.example.model.service.roleAccess;

import com.example.util.payload.dto.roleAccess.RoleAccessDto;

import java.util.List;

public interface RoleAccessService {

    List<RoleAccessDto> findRoleAccessByRole(String roleName);

}
