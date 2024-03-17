package com.example.util.payload.dto.document.excel;

import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;

public record ExcelExportHeadersAndByteStream(
        HttpHeaders headers,
        ByteArrayOutputStream stream
) {
}
