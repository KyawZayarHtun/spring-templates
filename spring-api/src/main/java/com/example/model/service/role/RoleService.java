package com.example.model.service.role;

import com.example.util.payload.dto.role.*;
import com.example.util.payload.dto.roleAccess.RoleAccessByRoleForm;
import com.example.util.payload.dto.table.TableResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    String getCurrentUserRoleName();

    String getCurrentUserRoleName(Authentication auth);

    Optional<RoleDetail> findByRoleId(Long id);

    Optional<RoleDetail> findByRoleName(String name);

    Long createRole(RoleCreateForm dto);

    Long updateRole(RoleUpdateForm dto) throws BadRequestException;

    TableResponse<RoleDetail> getRoleList(RoleSearchDto searchDto);

    boolean roleNameExist(String roleName);

    boolean roleNameExist(@Nullable Long id, String roleName);

    void saveRoleAccessByRoleId(RoleAccessByRoleForm form);
}
