package com.naveen.micro_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleType;
    private Integer yearsOfExperience;
    private Double rating;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
