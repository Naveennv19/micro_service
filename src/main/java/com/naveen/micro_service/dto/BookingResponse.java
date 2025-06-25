package com.naveen.micro_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String pickupLocation;
    private String dropLocation;
    private String date;
    private String time;
    private String status;
    private Long driverId;
    private String rideType;
    private String driverName;
    private String customerName;
}
