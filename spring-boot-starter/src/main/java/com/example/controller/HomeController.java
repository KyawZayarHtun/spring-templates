package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "Home";
    }

    @GetMapping("contact")
    public String contact() {
        return "Contact";
    }

    @GetMapping("about")
    public String about() {
        return "About";
    }

    @GetMapping("course")
    public String course() {
        return "Course";
    }

    @GetMapping("faq")
    public String faq() {
        return "Faq";
    }

    @GetMapping("terms-and-conditions")
    public String termsAndConditions() {
        return "Terms and Conditions";
    }

}
