package com.example.model.service.role;

import com.example.util.payload.dto.role.RoleAccessDetail;
import com.example.util.payload.dto.role.RoleForm;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.role.RoleSearchDto;
import com.example.util.payload.dto.table.TableResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    String getCurrentUserRoleName();

    String getCurrentUserRoleName(Authentication auth);

    Optional<RoleForm> findByRoleId(Long id);

    Optional<RoleForm> findByRoleName(String name);

    void manageRole(RoleForm dto);

    TableResponse<RoleListDto> getRoleList(RoleSearchDto searchDto);

    boolean roleNameExist(String roleName);

    boolean roleNameExist(@Nullable Long id, String roleName);

    List<RoleAccessDetail> roleDetailWithRoleAccess(Long id);
}
