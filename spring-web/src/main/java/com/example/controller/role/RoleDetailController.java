package com.example.controller.role;

import com.example.model.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleDetailController {

    private final RoleService roleService;

    @GetMapping("role-detail")
    public String roleDetail(@RequestParam Long id, ModelMap model) {

        var r = roleService.roleDetailWithRoleAccess(id);
        model.put("roleAccess", r);
        return "pages/role/role-detail";
    }

    @PostMapping("role-detail")
    public String manageRoleDetail() {

        return "redirect:/role/role-detail";
    }

}
