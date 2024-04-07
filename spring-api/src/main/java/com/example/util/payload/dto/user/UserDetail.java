package com.example.util.payload.dto.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.util.constant.Gender;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDetail(
        String name,
        String email,
        String phoneNo,
        Gender gender,
        LocalDate dob,
        Boolean locked,
        Boolean activated,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String profileImg
) {

    public static void select(CriteriaQuery<UserDetail> cq, Root<User> root) {
        cq.multiselect(
                root.get(User_.name),
                root.get(User_.email),
                root.get(User_.phoneNo),
                root.get(User_.gender),
                root.get(User_.dob),
                root.get(User_.locked),
                root.get(User_.activated),
                root.get(User_.createdAt),
                root.get(User_.updatedAt),
                root.get(User_.profileImagePath)
        );
    }

}
