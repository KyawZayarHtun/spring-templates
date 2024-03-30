package com.example.controller.role;

import com.example.model.service.role.RoleService;
import com.example.model.service.roleAccess.RoleAccessService;
import com.example.util.payload.dto.roleAccess.RoleAccessByRoleForm;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleDetailController {

    private final RoleService roleService;
    private final RoleAccessService roleAccessService;

    @GetMapping("role-detail")
    public String roleDetail(@RequestParam Long id, ModelMap model) {

        var roleAccessList = roleAccessService.convertToRoleAccessDetail(roleAccessService.findAllRoleAccess());
        var roleAccessIdListByRoleId = roleAccessService.findRoleAccessByRoleId(id)
                .stream().map(RoleAccessDto::id).toList();

        model.put("roleAccessForm", new RoleAccessByRoleForm(roleAccessIdListByRoleId));
        model.put("allRoleAccess", roleAccessList);
        return "pages/role/role-detail";
    }

    @PostMapping("role-detail")
    public String manageRoleDetail(@ModelAttribute("roleAccessForm") RoleAccessByRoleForm form, RedirectAttributes attr) {
        roleService.saveRoleAccessByRoleId(form);
        attr.addFlashAttribute("showToast", true);
        return "redirect:/role/role-detail?id=%d".formatted(form.getRoleId());
    }

}
