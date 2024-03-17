package com.example.util.payload.dto.document.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExcelColumnIndexAndValue {

    private Integer index;
    private Object value;

    public static ExcelColumnIndexAndValue fetchHeaders(ExcelColumn column) {
        return new ExcelColumnIndexAndValue(column.getIndex(), column.getColumnName());
    }
}
