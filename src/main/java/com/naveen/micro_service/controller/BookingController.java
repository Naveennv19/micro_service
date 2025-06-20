package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.BookingRequest;
import com.naveen.micro_service.dto.BookingResponse;
import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.Booking.BookingStatus;
import com.naveen.micro_service.model.Booking.BookingType;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.util.JwtAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;
    private final JwtAuthService jwtAuthService;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request, HttpServletRequest httpRequest) {
        // ✅ Get authenticated customer
        User customer = jwtAuthService.getAuthenticatedUser(httpRequest);

        if (!customer.getRole().equals(User.UserRole.CUSTOMER)) {
            return ResponseEntity.status(403).body("Only customers can book rides");
        }

        // ✅ Validate booking type
        BookingType type = request.getType();
        if (type == null) {
            return ResponseEntity.badRequest().body("Booking type is required");
        }

        // ✅ Combine date and time
        if (request.getDateTime() == null || request.getTime() == null) {
            return ResponseEntity.badRequest().body("Booking date and time are required");
        }

        LocalDateTime dateTime = LocalDateTime.of(request.getDateTime(), request.getTime());

        // ✅ Optionally handle return time (for outstation/rental if needed)
        LocalDateTime returnDateTime = null;
        if (request.getReturnDateTime() != null && request.getReturnTime() != null) {
            returnDateTime = LocalDateTime.of(request.getReturnDateTime(), request.getReturnTime());
        }

        // ✅ Build booking
        Booking booking = Booking.builder()
                .type(type)
                .pickupLocation(request.getPickupLoc())
                .dropLocation(request.getDropLoc())
                .packageHrs(request.getPackageHrs())
                .dateTime(dateTime)
                .returnDateTime(returnDateTime)
                .status(BookingStatus.PENDING)
                .customer(customer)
                .build();

        bookingRepository.save(booking);

        return ResponseEntity.ok(Map.of(
                "message", "Booking created successfully",
                "bookingId", booking.getId()
        ));
    }

    @PostMapping("/booking/filter")
    public ResponseEntity<?> getBookingsForCustomer(@RequestBody Map<String, String> filter,HttpServletRequest request) {

    User customer = jwtAuthService.getAuthenticatedUser(request);

    String statusStr = filter.get("bookingStatus");
    BookingStatus status = null;
    if (statusStr != null && !statusStr.isBlank()) {
        try {
            status = BookingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid booking status: " + statusStr);
        }
    }

    List<Booking> bookings;
    if (status != null) {
        bookings = bookingRepository.findByCustomerAndStatus(customer, status);
    } else {
        bookings = bookingRepository.findByCustomer(customer);
    }

    List<BookingResponse> response = bookings.stream().map(b -> BookingResponse.builder()
        .id(b.getId())
        .pickupLocation(b.getPickupLocation())
        .dropLocation(b.getDropLocation())
        .date(b.getDateTime().toLocalDate().toString())
        .time(b.getDateTime().toLocalTime().toString())
        .status(b.getStatus().toString())
        .driverId(b.getDriver() != null ? b.getDriver().getId() : null)
        .build()
    ).collect(Collectors.toList());
    return ResponseEntity.ok(response); 

    }
}
