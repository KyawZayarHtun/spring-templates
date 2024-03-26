package com.example.util.payload.dto.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.util.constant.RequestMethod;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;

public record RoleAccessListDto(
        Long id,
        String name,
        String url,
        RequestMethod requestMethod,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static void select(CriteriaQuery<RoleAccessListDto> cq, Root<RoleAccess> root) {
        cq.multiselect(
                root.get(RoleAccess_.id),
                root.get(RoleAccess_.name),
                root.get(RoleAccess_.url),
                root.get(RoleAccess_.requestMethod),
                root.get(RoleAccess_.description),
                root.get(RoleAccess_.createdAt),
                root.get(RoleAccess_.updatedAt)
        );
    }
}
