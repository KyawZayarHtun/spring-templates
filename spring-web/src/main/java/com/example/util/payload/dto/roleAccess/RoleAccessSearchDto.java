package com.example.util.payload.dto.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.util.payload.dto.table.EssentialDataForTable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Getter
public class RoleAccessSearchDto extends EssentialDataForTable {
    private String name;

    public Predicate[] predicates(CriteriaBuilder cb, Root<RoleAccess> root) {
        var list = new ArrayList<Predicate>();

        if (StringUtils.hasText(name))
            list.add(cb.like(cb.lower(root.get(RoleAccess_.name)), name.toLowerCase().concat("%")));

        return list.toArray(Predicate[]::new);
    }

}
