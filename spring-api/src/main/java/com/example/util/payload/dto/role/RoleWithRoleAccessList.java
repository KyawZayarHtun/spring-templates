package com.example.util.payload.dto.role;

import com.example.util.payload.dto.roleAccess.RoleAccessDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleWithRoleAccessList {

    private String name;
    private List<RoleAccessDetail> roleAccessList;

    public RoleWithRoleAccessList(Map.Entry<String, List<RoleAccessDetail>> dto) {
        this.name = dto.getKey();
        this.roleAccessList = dto.getValue();
    }
}
