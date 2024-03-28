package com.example.util.payload.dto.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.util.constant.CrudOperation;
import com.example.util.constant.RequestMethod;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleAccessForm {

    private Long id;

    @NotBlank(message = "Role Access name can't be blank!")
    private String name;

    @NotBlank(message = "Role Access url can't be blank!")
    private String url;

    @NotNull(message = "Request method can't be empty!")
    private RequestMethod requestMethod;

    @NotNull(message = "Request operation can't be empty!")
    private CrudOperation requestOperation;

    private String description;


    public static void select(CriteriaQuery<RoleAccessForm> cq, Root<RoleAccess> root) {
        cq.multiselect(
                root.get(RoleAccess_.id),
                root.get(RoleAccess_.name),
                root.get(RoleAccess_.url),
                root.get(RoleAccess_.requestMethod),
                root.get(RoleAccess_.crudOperation),
                root.get(RoleAccess_.description)
        );
    }
}
