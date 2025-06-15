package com.naveen.micro_service.controller;

import com.naveen.micro_service.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the Spring Boot App!";
    }

    @GetMapping("/user")
    public User getUser() {
        return new User(1L, "Naveen");
    }
}
