package com.example.controller.user;

import com.example.model.service.user.UserService;
import com.example.util.exception.handler.BindingResultHandler;
import com.example.util.payload.ApiResponse;
import com.example.util.payload.EmailRequest;
import com.example.util.payload.IdResponse;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserDetail;
import com.example.util.payload.dto.user.UserSearchDto;
import com.example.util.payload.dto.user.UserUpdateForm;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @PostMapping("user-list")
    public ApiResponse<TableResponse<UserDetail>> userList(@RequestBody @Valid UserSearchDto searchDto, BindingResult bindingResult) {
        BindingResultHandler.checkBindingResultError(bindingResult);
        return ApiResponse.success(userService.userList(searchDto));
    }

    @PostMapping("user-export")
    public ResponseEntity<byte[]> exportUser(@RequestBody UserSearchDto searchDto, HttpServletResponse response) throws IOException {
        var data = userService.generateExcelExport(searchDto, response);
        return ResponseEntity.ok()
                .headers(data.headers())
                .body(data.stream().toByteArray());
    }

    @GetMapping("user-detail")
    public ApiResponse<UserDetail> userDetail(@RequestBody @Valid EmailRequest req, BindingResult bindingResult) throws BadRequestException {
        BindingResultHandler.checkBindingResultError(bindingResult);
        return ApiResponse.success(userService.getUserByEmail(req.email()));
    }

    @GetMapping("user-profile")
    public ApiResponse<UserDetail> userProfile() {
        return ApiResponse.success(userService.getUserProfile());
    }

    @PostMapping("update-user")
    public ApiResponse<IdResponse> updateUser(@Valid UserUpdateForm form, BindingResult bindingResult) throws IOException {
        BindingResultHandler.checkBindingResultError(bindingResult);
        var id = userService.updateUserProfile(form);
        return ApiResponse.success(new IdResponse(id));
    }

}
