package com.naveen.micro_service.controller;


import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.CustomerRepository;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ✅ Get customer profile using JWT
    @GetMapping("/profile")
    public ResponseEntity<?> getCustomerProfile(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtUtil.extractEmail(token);
        System.out.println("Extracted email from token: " + email); // extract email from token

        User user = userRepository.findByEmail(email);
        System.out.println(user);
        System.out.println("Role: '" + user.getRole() + "'");
        if (user == null || user.getRole() != UserRole.CUSTOMER) {
            return ResponseEntity.status(403).body("Only customers can access this endpoint.");
        }


        return customerRepository.findByUserId(user.getId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get all bookings of a customer using customerId
    @GetMapping("/{id}/bookings")
    public ResponseEntity<?> getCustomerBookings(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Booking> bookings = bookingRepository.findByCustomerId(id);
        return ResponseEntity.ok(bookings);
    }

    // ✅ Extract Bearer token from Authorization header
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Skip "Bearer "
        } else {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
    }
}
