package com.naveen.micro_service.util;

import com.naveen.micro_service.model.User;
import com.naveen.micro_service.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email);
    }

    public String extractEmail(String token) {
        return jwtUtil.extractEmail(token); // or extractEmail if you use that key in your token
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Authorization header is missing or invalid");
    }
}
