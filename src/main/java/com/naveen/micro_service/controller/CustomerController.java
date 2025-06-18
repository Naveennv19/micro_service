package com.naveen.micro_service.controller;

import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ✅ Get customer profile using JWT token
    @GetMapping("/profile")
    public ResponseEntity<?> getCustomerProfile(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email);
        if (user == null || user.getRole() != UserRole.CUSTOMER) {
            return ResponseEntity.status(403).body("Only customers can access this endpoint.");
        }

        return ResponseEntity.ok(user);
    }

    // ✅ Get all bookings of a customer by customer ID
    @GetMapping("/{id}/bookings")
    public ResponseEntity<?> getCustomerBookings(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != UserRole.CUSTOMER) {
            return ResponseEntity.status(404).body("Customer not found with id: " + id);
        }

        List<Booking> bookings = bookingRepository.findAllByCustomerId(id);
        return ResponseEntity.ok(bookings);
    }

    // ✅ Extract Bearer token from Authorization header
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}
