package com.example.util.payload.dto.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.util.payload.dto.table.EssentialDataForTable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Getter
public class UserSearchDto extends EssentialDataForTable {

    private String name;

    public Predicate[] predicates(CriteriaBuilder cb, Root<User> root) {
        var list = new ArrayList<Predicate>();

        if (StringUtils.hasText(name))
            list.add(cb.like(cb.lower(root.get(User_.name)), name.toLowerCase().concat("%")));

        return list.toArray(Predicate[]::new);
    }
}
