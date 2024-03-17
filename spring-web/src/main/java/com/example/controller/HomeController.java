package com.example.controller;

import com.example.model.entity.User;
import com.example.model.repo.UserRepo;
import com.example.model.service.user.UserService;
import com.example.util.constant.Gender;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class HomeController {

    private final UserService userService;
    private final UserRepo repo;

    @GetMapping()
    public String home() {
       return "pages/home";
    }

    @GetMapping("customer")
    public String customer() {
       return "pages/customer";
    }

    @GetMapping("courses")
    public String courses() {
       return "pages/courses";
    }


}
