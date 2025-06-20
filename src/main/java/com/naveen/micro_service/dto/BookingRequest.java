package com.naveen.micro_service.dto;

import com.naveen.micro_service.model.Booking.BookingType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    private BookingType type;
    private String pickupLoc;
    private String dropLoc;
    private String packageHrs;
    private LocalDate dateTime;
    private LocalTime time;
    private LocalDate returnDateTime;
    private LocalTime returnTime;
}
