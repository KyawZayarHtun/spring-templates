package com.example.controller.user;

import com.example.model.service.user.UserService;
import com.example.util.payload.dto.user.UserDetailDtoForProfile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserProfileCreateController {

    private final UserService userService;

    @GetMapping("user-profile")
    public String profile() {
        return "pages/user-management/user-profile";
    }

    @PostMapping("edit-user-profile")
    public String editUserProfile(@ModelAttribute("userDetail") @Valid UserDetailDtoForProfile userDetail,
                                  BindingResult result,
                                  RedirectAttributes attr) throws IOException {
        if (result.hasErrors())
            return "pages/user-management/user-profile";
        userService.updateUserProfile(userDetail);
        attr.addFlashAttribute("showToast", true);
        return "redirect:/user/user-profile";
    }

    @ModelAttribute("userDetail")
    public UserDetailDtoForProfile userDetail() {
        return userService.getUserProfile();
    }

}
