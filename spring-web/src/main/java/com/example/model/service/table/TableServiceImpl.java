package com.example.model.service.table;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TableServiceImpl implements TableService{

    @Override
    public void sort(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root, String sortColumnName, String sortDir) {
        if (!StringUtils.hasText(sortDir))
            sortDir = "asc";

        if (!StringUtils.hasText(sortColumnName))
            sortColumnName = "name";

        switch (sortDir) {
            case "asc" -> cq.orderBy(cb.asc(root.get(sortColumnName)));
            case "desc" -> cq.orderBy(cb.desc(root.get(sortColumnName)));
        }
    }
    
}
