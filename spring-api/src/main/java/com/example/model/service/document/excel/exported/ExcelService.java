package com.example.model.service.document.excel.exported;


import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.document.excel.ExcelSetting;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExcelService {

    ExcelExportHeadersAndByteStream generateExcel(ExcelSetting setting, HttpServletResponse response) throws IOException;

}
