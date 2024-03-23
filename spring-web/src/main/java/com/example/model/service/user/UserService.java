package com.example.model.service.user;

import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserDetailDtoForProfile;
import com.example.util.payload.dto.user.UserDetailDtoForSecurity;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    boolean isUniqueEmail(String email);

    TableResponse<UserListDto> userList(UserSearchDto searchDto);

    ExcelExportHeadersAndByteStream generateExcelExport(UserSearchDto searchDto,
                                                        HttpServletResponse response) throws IOException;

    Optional<UserDetailDtoForSecurity> getUserDetailByEmail(String email);

    String getLoginUserEmail();

    UserDetailDtoForProfile getUserProfile();

    String getLoginUsername();

    void updateUserProfile(UserDetailDtoForProfile userDetail) throws IOException;
}
