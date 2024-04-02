package com.example.util.payload.dto.document.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExcelSetting {

    private String title;
    private String sheetName;
    private List<ExcelRow> rows = new ArrayList<>();
    private List<Integer> excludeAutoSizeColumn = new ArrayList<>();

}
