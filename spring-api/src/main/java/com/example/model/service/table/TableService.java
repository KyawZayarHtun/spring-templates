package com.example.model.service.table;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface TableService {

    void sort(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root,
              String sortColumnName, String sortDir);

}
