package com.example.model.service.user;

import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    boolean emailExist(String email);

    TableResponse<UserDetail> userList(UserSearchDto searchDto);

    ExcelExportHeadersAndByteStream generateExcelExport(UserSearchDto searchDto,
                                                        HttpServletResponse response) throws IOException;

    Optional<UserDetailDtoForSecurity> getUserDetailByEmail(String email);

    UserDetail getUserByEmail(String email) throws BadRequestException;

    String getLoginUserEmail();

    UserDetail getUserProfile();

    String getLoginUsername();

    Long updateUserProfile(UserUpdateForm form) throws IOException;
}
