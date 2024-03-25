package com.example.util.payload.dto.role;

import com.example.util.validation.roleNameUnique.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleForm {

    private Long id;

    @NotBlank(message = "Name can not be blank!")
    @UniqueRoleName(message = "Role Name already exists!")
    private String name;

}
