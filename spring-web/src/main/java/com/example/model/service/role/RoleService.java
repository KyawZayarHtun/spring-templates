package com.example.model.service.role;

import com.example.util.payload.dto.role.RoleForm;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.role.RoleSearchDto;
import com.example.util.payload.dto.table.TableResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RoleService {

    String getCurrentUserRoleName();

    String getCurrentUserRoleName(Authentication auth);

    Optional<RoleForm> findByRoleId(Long id);

    void manageRole(RoleForm dto);

    TableResponse<RoleListDto> getRoleList(RoleSearchDto searchDto);

    boolean roleNameExist(String roleName);
}
