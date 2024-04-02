package com.example.util.document.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ApachePoiUtil {

    public Font excelFont(Workbook workbook, int fontSize) {
        var font = workbook.createFont();
        font.setFontName("Cambria");
        font.setFontHeightInPoints((short) fontSize);
        return font;
    }

    public CellStyle headerCellStyle(Workbook workbook, int fontSize) {
        var cellStyle = workbook.createCellStyle();
        var font = excelFont(workbook, fontSize);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public CellStyle dataCellStyle(Workbook workbook, int fontSize) {
        var cellStyle = workbook.createCellStyle();
        cellStyle.setFont(excelFont(workbook, fontSize));
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }



}
