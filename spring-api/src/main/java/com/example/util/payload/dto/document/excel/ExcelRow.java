package com.example.util.payload.dto.document.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ExcelRow {

    private List<ExcelColumn> excelColumns = new ArrayList<>();

    public void addExcelColumn(ExcelColumn... columns) {
        this.excelColumns = new ArrayList<>(Arrays.asList(columns));
    }
}
