package com.example.util.payload.dto.table;

import java.util.List;

public record TableResponse<T>(
        long totalCount,
        long filterCount,
        int totalPages,
        List<T> contents
) {
}
