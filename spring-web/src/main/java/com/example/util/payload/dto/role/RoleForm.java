package com.example.util.payload.dto.role;

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
    private String name;

}
