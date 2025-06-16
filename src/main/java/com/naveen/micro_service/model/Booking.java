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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each booking is linked to a single customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // airport, local, outstation, hourly
    @Column(nullable = false)
    private String type;

    @Column(name = "pickup_loc", nullable = false)
    private String pickupLoc;

    @Column(name = "drop_loc")
    private String dropLoc;

    @Column(name = "package_hrs")
    private String packageHrs;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "return_date_time")
    private LocalDateTime returnDateTime;

    @Column(nullable = false)
    private String status; // pending, confirmed, cancelled, etc.

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
