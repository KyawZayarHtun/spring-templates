package com.example.model.service.role;

import com.example.util.payload.dto.general.IdAndNameDto;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.role.RoleSearchDto;
import com.example.util.payload.dto.table.TableResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RoleService {

    String getCurrentUserRoleName();

    String getCurrentUserRoleName(Authentication auth);

    Optional<IdAndNameDto> findByRoleId(Long id);

    void manageService(IdAndNameDto dto);

    TableResponse<RoleListDto> getRoleList(RoleSearchDto searchDto);

    boolean roleNameExist(String roleName);
}
