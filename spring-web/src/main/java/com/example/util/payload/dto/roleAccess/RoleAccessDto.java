package com.example.util.payload.dto.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.util.constant.RequestMethod;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public record RoleAccessDto(
        String name,
        String url,
        RequestMethod requestMethod,
        String description) {

    public static void select(CriteriaQuery<RoleAccessDto> cq, Root<RoleAccess> root) {
        cq.multiselect(
                root.get(RoleAccess_.name),
                root.get(RoleAccess_.url),
                root.get(RoleAccess_.requestMethod),
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
