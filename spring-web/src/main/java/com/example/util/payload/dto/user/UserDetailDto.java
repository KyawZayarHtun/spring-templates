package com.example.util.payload.dto.user;

import com.example.model.entity.Role_;
import com.example.model.entity.User;
import com.example.model.entity.User_;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public record UserDetailDto(
    String name,
    String email,
    String password,
    String role,
    boolean isLocked,
    boolean isActivated
) {


    public static void select(CriteriaQuery<UserDetailDto> cq, Root<User> root) {
        var role = root.join(User_.role);
        cq.multiselect(
                root.get(User_.name),
                root.get(User_.email),
                root.get(User_.password),
                role.get(Role_.name),
                root.get(User_.locked),
                root.get(User_.activated)
        );
    }
}
