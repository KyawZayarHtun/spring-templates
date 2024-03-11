package com.example.util.payload.dto.table;

import lombok.Getter;

@Getter
public abstract class EssentialDataForTable {

    private int pageNo;
    private int size;
    private String sortColumnName;
    private String sortDir;

}
