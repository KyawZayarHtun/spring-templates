package com.example.controller.roleAccess;

import com.example.model.service.role.RoleService;
import com.example.model.service.roleAccess.RoleAccessService;
import com.example.util.exception.handler.BindingResultHandler;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.IdResponse;
import com.example.util.payload.dto.roleAccess.*;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("role-access")
public class RoleAccessController {

    private final RoleService roleService;
    private final RoleAccessService roleAccessService;

    @GetMapping("current-role-access")
    public ApiResponse<List<RoleAccessDetail>> accessUrls(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("You don't have access for this route!");
        String role = roleService.getCurrentUserRoleName(auth);
        return ApiResponse.success(roleAccessService.findRoleAccessByRoleName(role));
    }

    @GetMapping("role-access-detail")
    public ApiResponse<RoleAccessDetail> findByRoleAccessId(@RequestParam Long id) throws BadRequestException {
        var dto = roleAccessService.findRoleAccessById(id).orElseThrow(() -> new BadRequestException("Given id doesn't exist!"));
        return ApiResponse.success(dto);
    }

    @PostMapping("/create-role-access")
    public ApiResponse<IdResponse> createRoleAccess(@RequestBody @Valid RoleAccessCreateForm dto,
                                                    BindingResult bindingResult) {

        checkRoleAccessNameExists(null, dto.name(), bindingResult);
        BindingResultHandler.checkBindingResultError(bindingResult);

        var id = roleAccessService.createRoleAccess(dto);
        return ApiResponse.success(new IdResponse(id));
    }

    @PutMapping("/update-role-access")
    public ApiResponse<IdResponse> updateRoleAccess(@RequestBody @Valid RoleAccessUpdateForm dto,
                                                    BindingResult bindingResult) throws BadRequestException {

        checkRoleAccessNameExists(dto.id(), dto.name(), bindingResult);
        BindingResultHandler.checkBindingResultError(bindingResult);

        var id = roleAccessService.updateRoleAccess(dto);
        return ApiResponse.success(new IdResponse(id));
    }

    @DeleteMapping("/delete-role-access")
    public ApiResponse<String> deleteRoleAccess(@RequestParam Long id) throws BadRequestException {
        roleAccessService.findRoleAccessById(id).orElseThrow(() -> new BadRequestException("Given id is wrong"));
        roleAccessService.deleteRoleAccessWithAllInheritance(id);
        return ApiResponse.success("Delete Successfully.");
    }

    @PostMapping("/role-access-list")
    public ApiResponse<TableResponse<RoleAccessDetail>> roleList(@RequestBody @Valid RoleAccessSearchDto searchDto, BindingResult bindingResult) {
        BindingResultHandler.checkBindingResultError(bindingResult);
        return ApiResponse.success(roleAccessService.getRoleAccessList(searchDto));
    }

    private void checkRoleAccessNameExists(Long id, String name, BindingResult bindingResult) {
        if (roleAccessService.roleAccessNameExists(id, name))
            bindingResult.rejectValue("name", "", "Role Access name is already exist!");
    }

}
