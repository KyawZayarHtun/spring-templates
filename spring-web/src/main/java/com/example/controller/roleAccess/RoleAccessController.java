package com.example.controller.roleAccess;

import com.example.model.service.roleAccess.RoleAccessService;
import com.example.util.constant.CrudOperation;
import com.example.util.constant.RequestMethod;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;
import com.example.util.payload.dto.roleAccess.RoleAccessListDto;
import com.example.util.payload.dto.roleAccess.RoleAccessSearchDto;
import com.example.util.payload.dto.table.TableResponse;
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

    @GetMapping("/manage-role-access")
    public String roleManagement(@RequestParam(required = false) Long id, ModelMap model) {
        if (id == null || id <= 0) {
            model.put("title", "Create");
        } else {
            model.put("title", "Update");
        }
        model.addAttribute("requestMethods", RequestMethod.values());
        model.addAttribute("requestOperations", CrudOperation.values());
        return "pages/role-access/manage-role-access";
    }

    @PostMapping("/manage-role-access")
    public String manageRoleAccess(@ModelAttribute("roleAccessObject") @Valid RoleAccessForm dto,
                                   BindingResult result, RedirectAttributes attr,
                                   ModelMap model) {

        if (roleAccessService.roleAccessNameExists(dto.getId(), dto.getName()))
            result.rejectValue("name", "", "Role Access name is already exist!");

        if (result.hasErrors()) {
            model.addAttribute("requestMethods", RequestMethod.values());
            return "pages/role-access/manage-role-access";
        }

        roleAccessService.manageRoleAccess(dto);

        if (dto.getId() == null || dto.getId() <= 0) {
            attr.addFlashAttribute("prevTitle", "Create");
        } else {
            attr.addFlashAttribute("prevTitle", "Update");
        }

        attr.addFlashAttribute("showToast", true);
        return "redirect:/role-access/manage-role-access";
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

    @ResponseBody
    @PostMapping("/role-access-list")
    public TableResponse<RoleAccessListDto> roleList(@RequestBody RoleAccessSearchDto searchDto) {
        return roleAccessService.getRoleAccessList(searchDto);
    }
}
