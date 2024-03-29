package com.example.util.payload.dto.roleAccess;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleAccessByRoleForm {

    private Long roleId;
    private List<Long> idList = new ArrayList<>();

    public RoleAccessByRoleForm(List<Long> ids) {
        idList.addAll(ids);
    }

}
