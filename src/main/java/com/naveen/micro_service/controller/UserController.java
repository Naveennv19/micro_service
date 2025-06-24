package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.ApiResponse;
import com.naveen.micro_service.dto.LoginRequest;
import com.naveen.micro_service.dto.UserDto;
import com.naveen.micro_service.dto.UserResponse;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.service.UserService;
import com.naveen.micro_service.util.JwtUtil;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 🟢 REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    // 🟢 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }


    // 🟢 LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok("Successfully logged out. Please delete the token on the client side.");
    }



    // 🟢 GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("success")
                .message("User fetched successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 🟢 FULL UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        User savedUser = userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("success")
                .message("User updated successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 🟢 PARTIAL UPDATE
    @PatchMapping("/patch/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> partiallyUpdateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("password")) {
            user.setPassword((String) updates.get("password"));
        }

        User savedUser = userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("success")
                .message("User updated successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
