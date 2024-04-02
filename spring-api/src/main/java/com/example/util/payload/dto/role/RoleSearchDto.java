package com.example.util.payload.dto.role;

import com.example.model.entity.Role;
import com.example.model.entity.Role_;
import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.util.payload.dto.table.EssentialDataForTable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Getter
public class RoleSearchDto extends EssentialDataForTable {
    private String name;

    public Predicate[] predicates(CriteriaBuilder cb, Root<Role> root) {
        var list = new ArrayList<Predicate>();

        if (StringUtils.hasText(name))
            list.add(cb.like(cb.lower(root.get(Role_.name)), name.toLowerCase().concat("%")));

        return list.toArray(Predicate[]::new);
    }

}
