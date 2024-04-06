package com.example.controller.role;

import com.example.model.service.role.RoleService;
import com.example.util.exception.handler.BindingResultHandler;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.IdResponse;
import com.example.util.payload.dto.role.*;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("role-list")
    public ApiResponse<TableResponse<RoleDetail>> roleList(@RequestBody @Valid RoleSearchDto searchDto, BindingResult bindingResult) {
        BindingResultHandler.checkBindingResultError(bindingResult);
        return ApiResponse.success(roleService.getRoleList(searchDto));
    }

    @PostMapping("create-role")
    public ApiResponse<IdResponse> createRole(@RequestBody @Valid RoleCreateForm dto, BindingResult bindingResult) {

        checkRoleNameExists(null, dto.name(), bindingResult);
        BindingResultHandler.checkBindingResultError(bindingResult);
        var id = roleService.createRole(dto);
        return ApiResponse.success(new IdResponse(id));
    }

    @PostMapping("update-role")
    public ApiResponse<IdResponse> updateRole(@RequestBody @Valid RoleUpdateForm dto, BindingResult bindingResult) throws BadRequestException {
        checkRoleNameExists(dto.id(), dto.name(), bindingResult);
        BindingResultHandler.checkBindingResultError(bindingResult);
        var id = roleService.updateRole(dto);
        return ApiResponse.success(new IdResponse(id));
    }

    @GetMapping("role-detail")
    public ApiResponse<RoleDetail> roleDetail(@RequestParam Long id) throws BadRequestException {
        var dto = roleService.findByRoleId(id).orElseThrow(() -> new BadRequestException("Given id doesn't exist!"));
//        var a = roleService.find
        return ApiResponse.success(dto);
    }

    private void checkRoleNameExists(Long id, String name, BindingResult bindingResult) {
        if (roleService.roleNameExist(id, name))
            bindingResult.rejectValue("name", "", "Role name is already exist!");
    }

}
