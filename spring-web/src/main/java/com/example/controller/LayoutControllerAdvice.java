package com.example.controller;

import com.example.util.constant.RequestMethod;
import com.example.util.payload.dto.role.RoleAccessDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class LayoutControllerAdvice {

    @ModelAttribute("accessUrls")
    public List<RoleAccessDto> accessUrls() {
        return List.of(
                new RoleAccessDto("/", RequestMethod.GET),
                new RoleAccessDto("/customer", RequestMethod.GET),
                new RoleAccessDto("/courses", RequestMethod.GET)
        );
    }

}
