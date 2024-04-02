package com.example.util.payload.dto.role;

import com.example.model.entity.Role;
import com.example.model.entity.Role_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public record RoleListDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static void select(CriteriaQuery<RoleListDto> cq, Root<Role> root) {
        cq.multiselect(
                root.get(Role_.id),
                root.get(Role_.name),
                root.get(Role_.createdAt),
                root.get(Role_.updatedAt)
        );
    }


}
