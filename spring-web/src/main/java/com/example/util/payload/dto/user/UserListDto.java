package com.example.util.payload.dto.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.util.constant.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserListDto(
        String name,
        String email,
        String phoneNo,
        Gender gender,
        LocalDate dob,
        Boolean locked,
        Boolean activated,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static void select(CriteriaQuery<UserListDto> cq, Root<User> root) {
        cq.multiselect(
                root.get(User_.name),
                root.get(User_.email),
                root.get(User_.phoneNo),
                root.get(User_.gender),
                root.get(User_.dob),
                root.get(User_.locked),
                root.get(User_.activated),
                root.get(User_.createdAt),
                root.get(User_.updatedAt)
        );
    }


    public static void sort(CriteriaBuilder cb, CriteriaQuery<UserListDto> cq, Root<User> root,
                            String sortColumnName, String sortDir) {

        if (!StringUtils.hasText(sortDir))
            sortDir = "asc";

        if (!StringUtils.hasText(sortColumnName))
            sortColumnName = "id";

        switch (sortDir) {
            case "asc" -> cq.orderBy(cb.asc(root.get(sortColumnName)));
            case "desc" -> cq.orderBy(cb.desc(root.get(sortColumnName)));
        }

    }
}
