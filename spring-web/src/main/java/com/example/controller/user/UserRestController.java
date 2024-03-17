package com.example.controller.user;

import com.example.model.service.user.UserService;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    @PostMapping("list")
    public TableResponse<UserListDto> userList(@RequestBody UserSearchDto searchDto) {
        return userService.userList(searchDto);
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportAgentType(@RequestBody UserSearchDto searchDto,
                                                  HttpServletResponse response) throws IOException {
        var data = userService.generateExcelExport(response);
       return ResponseEntity.ok()
               .headers(data.headers())
               .body(data.stream().toByteArray());
    }

}
