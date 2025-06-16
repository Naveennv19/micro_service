package com.naveen.micro_service.controller;

import org.springframework.web.bind.annotation.*;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

}
