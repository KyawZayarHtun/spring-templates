package com.example.model.service.roleAccess;

import com.example.util.payload.dto.roleAccess.*;
import com.example.util.payload.dto.table.TableResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface RoleAccessService {

    List<RoleAccessDto> findRoleAccessByRoleId(Long roleId);
    List<RoleAccessDto> findRoleAccessByRoleName(String roleName);

    Optional<RoleAccessForm> findRoleAccessById(Long id);

    Optional<RoleAccessForm> findRoleAccessByName(String roleAccessName);

    void manageRoleAccess(RoleAccessForm dto);

    boolean roleAccessNameExists(@Nullable Long id, String roleAccessName);

    TableResponse<RoleAccessListDto> getRoleAccessList(RoleAccessSearchDto searchDto);

    List<RoleAccessDto> findAllRoleAccess();

    List<RoleAccessDetail> convertToRoleAccessDetail(List<RoleAccessDto> roleAccessList);

}
