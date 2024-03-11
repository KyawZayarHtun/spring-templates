package com.example.model.service.user;

import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;

import java.util.List;

public interface UserService {
    boolean isUniqueEmail(String email);

    TableResponse<UserListDto> userList(UserSearchDto searchDto);
}
