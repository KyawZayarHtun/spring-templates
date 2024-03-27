package com.example.util.payload.dto.role;

import com.example.util.payload.dto.roleAccess.RoleAccessDto;
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
public class RoleAccessDetail {

    private String name;
    private List<RoleAccessDto> roleAccessList;

    /*public RoleAccessDetail(Map<String, List<RoleAccessDto>> dto) {
        this.name = dto.get();
        this.roleAccessList = roleAccessList;
    }*/
}
