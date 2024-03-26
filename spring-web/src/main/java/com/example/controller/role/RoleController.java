package com.example.controller.role;

import com.example.model.service.role.RoleService;
import com.example.util.payload.dto.role.RoleForm;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.role.RoleSearchDto;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("role-management")
    public String roleManagement(@RequestParam(required = false) Long id, ModelMap model) {
        if (id == null || id <= 0) {
            model.put("title", "Create");
        } else {
            model.put("title", "Update");
        }
        return "pages/role/role-management";
    }

    @PostMapping("manage-role")
    public String createRole(@ModelAttribute("roleObject") @Valid RoleForm dto,
                             BindingResult result,
                             RedirectAttributes attr) {

        if (roleService.roleNameExist(dto.getId(), dto.getName()))
            result.rejectValue("name", "", "Role Name already exists!");

        if (result.hasErrors())
            return "pages/role/role-management";

        roleService.manageRole(dto);

        if (dto.getId() == null || dto.getId() <= 0) {
            attr.addFlashAttribute("prevTitle", "Create");
        } else {
            attr.addFlashAttribute("prevTitle", "Update");
        }
        attr.addFlashAttribute("showToast", true);

        return "redirect:/role/role-management";
    }

    @ModelAttribute("roleObject")
    public RoleForm roleObject(@RequestParam(required = false) Long id) throws BadRequestException {
        if (id == null || id <= 0)
            return new RoleForm();

        var dto = roleService.findByRoleId(id);
        if (dto.isEmpty())
            throw new BadRequestException("Given Id is wrong");
        return dto.get();
    }

    @ResponseBody
    @PostMapping("role-list")
    public TableResponse<RoleListDto> roleList(@RequestBody RoleSearchDto searchDto) {
        return roleService.getRoleList(searchDto);
    }


}
