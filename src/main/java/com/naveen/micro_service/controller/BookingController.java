package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.BookingRequest;
import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.Customer;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.CustomerRepository;

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

    @PostMapping("/trip-type")
    public ResponseEntity<?> selectTripType(@RequestBody Map<String, String> body) {
        String tripType = body.get("tripType");
        List<String> validTypes = List.of("airport-transfer", "local-travel", "outstation-travel", "hourly-rentals");

        if (!validTypes.contains(tripType)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid trip type"));
        }

        return ResponseEntity.ok(Map.of("message", "Trip type selected: " + tripType));
    }

    @PostMapping("/airport")
    public ResponseEntity<?> bookAirport(@RequestBody BookingRequest request) {
        return createBooking(request, "airport");
    }

    @PostMapping("/local")
    public ResponseEntity<?> bookLocal(@RequestBody BookingRequest request) {
        return createBooking(request, "local");
    }

    @PostMapping("/outstation-oneway")
    public ResponseEntity<?> bookOutstationOneWay(@RequestBody BookingRequest request) {
        return createBooking(request, "outstation");
    }

    @PostMapping("/outstation-twoway")
    public ResponseEntity<?> bookOutstationTwoWay(@RequestBody BookingRequest request) {
        if (request.getReturnDateTime() == null || request.getReturnTime() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Return date/time is required"));
        }
        return createBooking(request, "outstation");
    }

    @PostMapping("/hourly")
    public ResponseEntity<?> bookHourly(@RequestBody BookingRequest request) {
        return createBooking(request, "hourly");
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<?> getDashboard(@PathVariable Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        long totalTrips = bookingRepository.countByCustomerId(customer.getId());

        List<Booking> recentTrips = bookingRepository
                .findTop3ByCustomerIdOrderByCreatedAtDesc(customer.getId());

        return ResponseEntity.ok(Map.of(
                "totalTrips", totalTrips,
                "recentTrips", recentTrips
        ));
    }

    private ResponseEntity<?> createBooking(BookingRequest req, String type) {
        if (req.getCustomerId() == null || req.getPickupLoc() == null || req.getDateTime() == null || req.getTime() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", type + " booked successfully"));
    }
}
