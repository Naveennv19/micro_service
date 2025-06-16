package com.naveen.micro_service.controller;

import org.springframework.web.bind.annotation.*;

import com.naveen.micro_service.dto.LoginRequest;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

}
