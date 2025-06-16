package com.naveen.micro_service.controller;

import org.springframework.web.bind.annotation.*;

import com.naveen.micro_service.dto.ApiResponse;
import com.naveen.micro_service.dto.LoginRequest;
import com.naveen.micro_service.dto.UserResponse;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtUtil;

import java.util.Map;

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

    @PostMapping("/logout")
    public String logoutUser() {
        // Instruct client to delete the token
        return "Successfully logged out. Please delete the token on the client side.";
    }


    @GetMapping("/user/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return ApiResponse.<UserResponse>builder()
                .status("success")
                .message("User fetched successfully")
                .data(response)
                .build();
    }

    @PutMapping("/userUpdate/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword()); // You may hash this in real scenarios

        User savedUser = userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();

        return ApiResponse.<UserResponse>builder()
                .status("success")
                .message("User updated successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/userReqUpdate/{id}")
public ApiResponse<UserResponse> partiallyUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    // Update only provided fields
    if (updates.containsKey("name")) {
        user.setName((String) updates.get("name"));
    }
    if (updates.containsKey("email")) {
        user.setEmail((String) updates.get("email"));
    }
    if (updates.containsKey("password")) {
        user.setPassword((String) updates.get("password")); // hash it in real apps
    }

    User savedUser = userRepository.save(user);

    UserResponse response = UserResponse.builder()
            .id(savedUser.getId())
            .name(savedUser.getName())
            .email(savedUser.getEmail())
            .build();

    return ApiResponse.<UserResponse>builder()
            .status("success")
            .message("User updated successfully")
            .data(response)
            .build();
}

}
