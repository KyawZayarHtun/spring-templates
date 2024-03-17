package com.example.model.service.document.excel.exported;

import com.example.util.document.excel.ApachePoiUtil;
import com.example.util.payload.dto.document.excel.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final ApachePoiUtil apachePoiUtil;

    @Value("${spring.mvc.format.date-time}")
    private String dateTimeFormat;

    @Override
    public ExcelExportHeadersAndByteStream generateExcel(ExcelSetting excelSetting, HttpServletResponse response) throws IOException {
        var workbook = new XSSFWorkbook();


        var headers = excelSetting.getRows().getFirst().getExcelColumns()
                .stream()
                .map(ExcelColumnIndexAndValue::fetchHeaders)
                .toList();

        // Response
//        setNecessaryResponseSetting(excelSetting.getSheetName(), response);

        // Create Excel Sheet
        XSSFSheet sheet = workbook.createSheet(excelSetting.getSheetName());

        // Column Last Index
        int lastColIndex = headers.size() - 1;

        // Create Title
        createTitle(excelSetting.getTitle(), sheet, lastColIndex, workbook);

        // Create Date
        createDate(sheet, lastColIndex, workbook);

        // Create Header
        createHeader(headers, sheet, workbook);

        // Create Data
        createData(excelSetting.getRows(), workbook, sheet);

        // Column width setting
        columnWidthSetting(excelSetting, headers, sheet);

        /*ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();*/

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);

        return new ExcelExportHeadersAndByteStream(getHeaders(excelSetting.getSheetName()), stream);
    }

    private HttpHeaders getHeaders(String sheetName) {
        String fileName = String.format("%s-%s.xlsx",
                sheetName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", fileName);
        return headers;
    }

    private void createTitle(String title, XSSFSheet sheet, int lastColIndex, XSSFWorkbook workbook) {
        var titleRow = sheet.createRow(0);
        var titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        sheet.addMergedRegion(
                new CellRangeAddress(0, 1, 0, lastColIndex));
        CellStyle titleStyle = apachePoiUtil.headerCellStyle(workbook, 21);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);
    }

    private void createDate(XSSFSheet sheet, int lastColIndex, XSSFWorkbook workbook) {
        var dateRow = sheet.createRow(2);
        var dateCell = dateRow.createCell(lastColIndex);
        dateCell.setCellValue(LocalDateTime.now());
        CreationHelper createHelper = workbook.getCreationHelper();
        var dateStyle = apachePoiUtil.headerCellStyle(workbook, 11);
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yyyy h:mm AM/PM"));
        dateCell.setCellStyle(dateStyle);
    }

    private void createHeader(List<ExcelColumnIndexAndValue> headers,
                              XSSFSheet sheet,
                              XSSFWorkbook workbook) {
        XSSFRow headerRow = sheet.createRow(3);
        for (var column : headers) {
            XSSFCell headerCell = headerRow.createCell(column.getIndex());
            headerCell.setCellValue((String) column.getValue());
            headerCell.setCellStyle(apachePoiUtil.headerCellStyle(workbook, 12));
        }
    }

    private void createData(List<ExcelRow> rows, XSSFWorkbook workbook, XSSFSheet sheet) {
        AtomicInteger dataRowIndex = new AtomicInteger(4);
        CellStyle dataCellStyle = apachePoiUtil.dataCellStyle(workbook, 11);

        for (ExcelRow row : rows) {
            XSSFRow dataRow = sheet.createRow(dataRowIndex.get());
            for (ExcelColumn excelColumn : row.getExcelColumns()) {
                var cell = dataRow.createCell(excelColumn.getIndex());
                createCellValue(excelColumn.getColumnValue(), cell, dataCellStyle, workbook);
                cell.setCellStyle(dataCellStyle);
            }
            dataRowIndex.getAndIncrement();
        }
    }

    private void createCellValue(Object columnName, XSSFCell cell, CellStyle style,
                                 XSSFWorkbook workbook) {
        if (columnName instanceof Integer || columnName instanceof Long) {
            cell.setCellValue(String.valueOf(columnName));
        } else if (columnName instanceof Boolean) {
            cell.setCellValue((Boolean) columnName);
        } else if (columnName instanceof String) {
            cell.setCellValue((String) columnName);
        } else if (columnName instanceof LocalDate) {
            CreationHelper createHelper = workbook.getCreationHelper();
            style.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy"));
            cell.setCellValue((LocalDate) columnName);
        } else {
            CreationHelper createHelper = workbook.getCreationHelper();
            style.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yyyy h:mm AM/PM"));
            cell.setCellValue((LocalDateTime) columnName);
        }
    }

    private void columnWidthSetting(ExcelSetting excelSetting,
                                    List<ExcelColumnIndexAndValue> headers, XSSFSheet sheet) {
        List<ExcelColumnIndexAndValue> autoWidthColumns = new ArrayList<>();

        if (!excelSetting.getExcludeAutoSizeColumn().isEmpty()) {
            for (var header : headers) {
                for (var index : excelSetting.getExcludeAutoSizeColumn()) {
                    if (!header.getIndex().equals(index))
                        autoWidthColumns.add(header);
                }
            }
        } else {
            autoWidthColumns.addAll(headers);
        }


        autoWidthColumns.forEach(column -> sheet.autoSizeColumn(column.getIndex()));

        if (!excelSetting.getExcludeAutoSizeColumn().isEmpty())
            excelSetting.getExcludeAutoSizeColumn().forEach(index -> sheet.setColumnWidth(index, 7000));
    }

}

