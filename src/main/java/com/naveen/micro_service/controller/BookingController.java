package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.BookingRequest;
import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.Customer;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final JwtAuthService jwtAuthService;

    // ✅ Extract Bearer token from header
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }

    // ✅ Extract authenticated Customer using JwtAuthService
    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtAuthService.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null || user.getRole() != UserRole.CUSTOMER) {
            throw new RuntimeException("Only customers can access this endpoint");
        }

        return customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
    }

    // ✅ Trip type validation
    @PostMapping("/trip-type")
    public ResponseEntity<?> selectTripType(@RequestBody Map<String, String> body) {
        String tripType = body.get("tripType");
        List<String> validTypes = List.of("airport", "local", "outstation", "hourly");

        if (!validTypes.contains(tripType)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid trip type"));
        }

        return ResponseEntity.ok(Map.of("message", "Trip type selected: " + tripType));
    }

    // Booking endpoints using token-based customer identity
    @PostMapping("/airport")
    public ResponseEntity<?> bookAirport(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        return createBooking(request, httpRequest, "airport");
    }

    @PostMapping("/local")
    public ResponseEntity<?> bookLocal(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        return createBooking(request, httpRequest, "local");
    }

    @PostMapping("/outstation-oneway")
    public ResponseEntity<?> bookOutstationOneWay(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        return createBooking(request, httpRequest, "outstation");
    }

    @PostMapping("/outstation-twoway")
    public ResponseEntity<?> bookOutstationTwoWay(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        if (request.getReturnDateTime() == null || request.getReturnTime() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Return date/time is required"));
        }
        return createBooking(request, httpRequest, "outstation");
    }

    @PostMapping("/hourly")
    public ResponseEntity<?> bookHourly(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        return createBooking(request, httpRequest, "hourly");
    }

    // Customer Dashboard (secured)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        long totalTrips = bookingRepository.countByCustomerId(customer.getId());
        List<Booking> recentTrips = bookingRepository
                .findTop3ByCustomerIdOrderByCreatedAtDesc(customer.getId());

        return ResponseEntity.ok(Map.of(
                "totalTrips", totalTrips,
                "recentTrips", recentTrips
        ));
    }

    // Central booking logic
    private ResponseEntity<?> createBooking(BookingRequest req, HttpServletRequest httpRequest, String type) {
        if (req.getPickupLoc() == null || req.getDateTime() == null || req.getTime() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        Customer customer = getAuthenticatedCustomer(httpRequest);

        Booking booking = Booking.builder()
                .customer(customer)
                .type(type)
                .pickupLoc(req.getPickupLoc())
                .dropLoc(type.equals("hourly") ? req.getPickupLoc() : req.getDropLoc())
                .packageHrs(req.getPackageHrs())
                .dateTime(LocalDateTime.of(req.getDateTime(), req.getTime()))
                .returnDateTime((req.getReturnDateTime() != null && req.getReturnTime() != null)
                        ? LocalDateTime.of(req.getReturnDateTime(), req.getReturnTime()) : null)
                .status("pending")
                .build();

        bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", type + " booked successfully",
                "bookingId", booking.getId()
        ));
    }
}
