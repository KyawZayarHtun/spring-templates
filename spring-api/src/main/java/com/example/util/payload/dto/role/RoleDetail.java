package com.example.util.payload.dto.role;

import com.example.model.entity.Role;
import com.example.model.entity.Role_;
import com.example.util.payload.dto.roleAccess.RoleAccessDetail;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

public record RoleDetail(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static void select(CriteriaQuery<RoleDetail> cq, Root<Role> root) {
        cq.multiselect(
                root.get(Role_.id),
                root.get(Role_.name),
                root.get(Role_.createdAt),
                root.get(Role_.updatedAt)
        );
    }
}
