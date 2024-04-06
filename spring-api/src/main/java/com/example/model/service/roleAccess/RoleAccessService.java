package com.example.model.service.roleAccess;

import com.example.util.payload.dto.role.RoleWithRoleAccessList;
import com.example.util.payload.dto.roleAccess.*;
import com.example.util.payload.dto.table.TableResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface RoleAccessService {

    List<RoleAccessDetail> findRoleAccessByRoleId(Long roleId);
    List<RoleAccessDetail> findRoleAccessByRoleName(String roleName); // used

    Optional<RoleAccessDetail> findRoleAccessById(Long id); // used

    Optional<RoleAccessDetail> findRoleAccessByName(String roleAccessName);

    Long createRoleAccess(RoleAccessCreateForm dto); // used


    Long updateRoleAccess(RoleAccessUpdateForm dto); // used

    boolean roleAccessNameExists(@Nullable Long id, String roleAccessName); // used

    TableResponse<RoleAccessListDto> getRoleAccessList(RoleAccessSearchDto searchDto);

    List<RoleAccessDetail> findAllRoleAccess();

    List<RoleWithRoleAccessList> convertToRoleAccessDetail(List<RoleAccessDetail> roleAccessList);

    void deleteRoleAccessWithAllInheritance(Long roleAccessId);
}
