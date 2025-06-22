package com.naveen.micro_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    public enum BookingType {
        AIRPORT_TRAVEL,
        LOCAL_TRAVEL,
        OUTSTATION_TRAVEL,
        RENTAL_HOURS
    }

    public enum BookingStatus {
        PENDING,
        ASSIGNED,
        CURRENT,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingType type;

    @Column(name = "pickup_loc", nullable = false)
    private String pickupLocation;

    @Column(name = "drop_loc")
    private String dropLocation;

    @Column(name = "package_hrs")
    private String packageHrs;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "return_date_time")
    private LocalDateTime returnDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status; // PENDING, ASSIGNED, CURRENT, COMPLETED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;
}
