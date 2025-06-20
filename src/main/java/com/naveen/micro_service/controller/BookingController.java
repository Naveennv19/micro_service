package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.BookingRequest;
import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.Booking.BookingType;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final JwtAuthService jwtAuthService;

    // ✅ Extract Bearer token
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }

    // ✅ Authenticated CUSTOMER
    private User getAuthenticatedCustomer(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtAuthService.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null || user.getRole() != UserRole.CUSTOMER) {
            throw new RuntimeException("Only customers can book rides");
        }

        return user;
    }

    // ✅ Validate booking type (optional endpoint)
    @PostMapping("/validate-type")
    public ResponseEntity<?> validateType(@RequestBody Map<String, String> body) {
        String typeStr = body.get("tripType");
        try {
            BookingType type = BookingType.valueOf(typeStr.toUpperCase());
            return ResponseEntity.ok(Map.of("message", "Valid trip type: " + type.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid booking type"));
        }
    }

    // ✅ Single endpoint to handle booking based on BookingType
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest req, HttpServletRequest request) {
        if (req.getType() == null || req.getPickupLoc() == null || req.getDateTime() == null || req.getTime() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        User customer = getAuthenticatedCustomer(request);

        Booking booking = Booking.builder()
                .customer(customer)
                .type(req.getType())
                .pickupLoc(req.getPickupLoc())
                .dropLoc(req.getType() == BookingType.RENTAL_HOURS ? req.getPickupLoc() : req.getDropLoc())
                .packageHrs(req.getPackageHrs())
                .dateTime(LocalDateTime.of(req.getDateTime(), req.getTime()))
                .returnDateTime(
                        (req.getReturnDateTime() != null && req.getReturnTime() != null)
                                ? LocalDateTime.of(req.getReturnDateTime(), req.getReturnTime())
                                : null)
                .status("pending")
                .build();

        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", req.getType().name().toLowerCase().replace("_", " ") + " booked successfully",
                "bookingId", booking.getId()
        ));
    }

    // ✅ Dashboard for customers
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpServletRequest request) {
        User customer = getAuthenticatedCustomer(request);

        long totalTrips = bookingRepository.countByCustomer(customer);
        List<Booking> recentTrips = bookingRepository.findTop3ByCustomerOrderByCreatedAtDesc(customer);

        return ResponseEntity.ok(Map.of(
                "totalTrips", totalTrips,
                "recentTrips", recentTrips
        ));
    }
}
