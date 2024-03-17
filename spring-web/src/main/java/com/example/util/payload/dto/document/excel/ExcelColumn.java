package com.example.util.payload.dto.document.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelColumn {

    private Integer index;
    private String columnName;
    private Object columnValue;

}
