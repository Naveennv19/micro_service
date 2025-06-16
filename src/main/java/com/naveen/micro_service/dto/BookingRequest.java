package com.naveen.micro_service.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long customerId;

    private String pickupLoc;
    private String dropLoc;

    private String packageHrs;

    private LocalDate dateTime;
    private LocalTime time;

    private LocalDate returnDateTime;
    private LocalTime returnTime;
}
