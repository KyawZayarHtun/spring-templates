package com.example.model.service.user;

import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserDetailDto;
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

    Optional<UserDetailDto> getUserDetailByEmail(String email);
}
