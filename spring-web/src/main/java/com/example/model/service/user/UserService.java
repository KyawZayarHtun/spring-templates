package com.example.model.service.user;

import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface UserService {
    boolean isUniqueEmail(String email);

    TableResponse<UserListDto> userList(UserSearchDto searchDto);

    ExcelExportHeadersAndByteStream generateExcelExport(HttpServletResponse response) throws IOException;
}
