package com.example.util.payload.dto.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.util.constant.CrudOperation;
import com.example.util.constant.RequestMethod;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public record RoleAccessDetail(
        Long id,
        String name,
        String url,
        RequestMethod requestMethod,
        CrudOperation requestOperation,
        String description
) {

    public static void select(CriteriaQuery<RoleAccessDetail> cq, Root<RoleAccess> root) {
        cq.multiselect(
                root.get(RoleAccess_.id),
                root.get(RoleAccess_.name),
                root.get(RoleAccess_.url),
                root.get(RoleAccess_.requestMethod),
                root.get(RoleAccess_.crudOperation),
                root.get(RoleAccess_.description)
        );
    }

    public static String getUrlStartName(String url) {
        String[] split = url.split("/");
        if (split.length == 0)
            return "home";
        return split[1];
    }

}
