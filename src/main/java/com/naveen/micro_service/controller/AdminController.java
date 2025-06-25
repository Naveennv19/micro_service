package com.naveen.micro_service.controller;

import com.naveen.micro_service.dto.AssignDriverRequest;
import com.naveen.micro_service.dto.BookingResponse;
import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.BookingRepository;
import com.naveen.micro_service.repository.UserRepository;
import com.naveen.micro_service.util.JwtAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final JwtAuthService jwtAuthService;

    @PutMapping("/booking")
    public ResponseEntity<?> assignDriverToBooking(@RequestBody AssignDriverRequest request, HttpServletRequest httpRequest) {
        User admin = jwtAuthService.getAuthenticatedUser(httpRequest);

        if (admin.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admins can assign drivers.");
        }

        Booking booking = bookingRepository.findById(request.getBookingId()).orElse(null);
        if (booking == null) {
            return ResponseEntity.status(404).body("Booking not found");
        }

        User driver = userRepository.findById(request.getDriverId()).orElse(null);
        if (driver == null || driver.getRole() != UserRole.DRIVER) {
            return ResponseEntity.status(404).body("Driver not found or user is not a driver");
        }

        booking.setDriver(driver);
    booking.setStatus(Booking.BookingStatus.ASSIGNED);

    driver.setStatus(User.UserStatus.ON_TRIP); // ✅ this updates the driver's availability

    userRepository.save(driver);               // ✅ save the updated driver
    bookingRepository.save(booking);           // ✅ save the updated booking

    return ResponseEntity.ok(Map.of("message", "Driver assigned successfully"));
    }


    @GetMapping("/driver")
    public ResponseEntity<?> getDrivers(@RequestParam(required = false) String driverStatus, HttpServletRequest request) {
        User admin = jwtAuthService.getAuthenticatedUser(request);

        if (admin.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admins can view drivers.");
        }

        // Default fetch all drivers
        List<User> drivers;

        if (driverStatus != null && !driverStatus.isBlank()) {
            User.UserStatus status;
            try {
                status = User.UserStatus.valueOf(driverStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid driver status: " + driverStatus);
            }

            drivers = userRepository.findByRoleAndStatus(UserRole.DRIVER, status);
        } else {
            drivers = userRepository.findByRole(UserRole.DRIVER);
        }

        // Create a simplified driver response
       List<Map<String, Object>> response = drivers.stream().map(driver -> Map.<String, Object>of(
        "id", (Object) driver.getId(),
        "name", (Object) driver.getName(),
        "email", (Object) driver.getEmail(),
        "isAvailable", (Object) (driver.getStatus() == User.UserStatus.AVAILABLE)
        )).toList();

        return ResponseEntity.ok(response);


    }
    @GetMapping("/bookings")
public ResponseEntity<?> getAllBookings(HttpServletRequest request) {
    User admin = jwtAuthService.getAuthenticatedUser(request);

    if (admin == null || admin.getRole() != User.UserRole.ADMIN) {
        return ResponseEntity.status(403).body("Access denied. Only admins can view bookings.");
    }

    List<Booking> bookings = bookingRepository.findAll();

    List<BookingResponse> response = bookings.stream().map(b -> BookingResponse.builder()
            .id(b.getId())
            .pickupLocation(b.getPickupLocation())
            .dropLocation(b.getDropLocation())
            .date(b.getDateTime().toLocalDate().toString())
            .time(b.getDateTime().toLocalTime().toString())
            .status(b.getStatus().toString().toLowerCase())
            .rideType(b.getType().toString().toLowerCase())
            .driverId(b.getDriver() != null ? b.getDriver().getId() : null)
            .driverName(b.getDriver() != null ? b.getDriver().getName() : null)
            .customerName(b.getCustomer().getName())
            .build())
        .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}


}
