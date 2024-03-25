package com.example.controller.roleAccess;

import com.example.model.service.roleAccess.RoleAccessService;
import com.example.util.constant.RequestMethod;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/role-access")
public class RoleAccessController {

    private final RoleAccessService roleAccessService;

    @GetMapping("/role-access-management")
    public String roleManagement(@RequestParam(required = false) Long id, ModelMap model) {
        if (id == null || id <= 0) {
            model.put("title", "Create");
        } else {
            model.put("title", "Update");
        }
        model.addAttribute("requestMethods", RequestMethod.values());
        return "pages/role-access/role-access-management";
    }

    @PostMapping("/manage-role-access")
    public String manageRoleAccess(@ModelAttribute("roleAccessObject") @Valid RoleAccessForm dto,
                                   BindingResult result, RedirectAttributes attr,
                                   ModelMap model) {

        if (result.hasErrors()) {
            model.addAttribute("requestMethods", RequestMethod.values());
            return "pages/role-access/role-access-management";
        }

        roleAccessService.manageRoleAccess(dto);

        if (dto.getId() == null || dto.getId() <= 0) {
            attr.addFlashAttribute("title", "Create");
        } else {
            attr.addFlashAttribute("title", "Update");
        }

        attr.addFlashAttribute("showToast", true);
        return "redirect:/role-access/role-access-management";
    }

    @ModelAttribute("roleAccessObject")
    public RoleAccessForm roleAccessObject(@RequestParam(required = false) Long id) throws BadRequestException {
        if (id == null)
            return new RoleAccessForm();

        Optional<RoleAccessForm> dto = roleAccessService.findRoleAccessById(id);
        if (dto.isEmpty())
            throw new BadRequestException("Given Id is wrong");
        return dto.get();
    }


}
