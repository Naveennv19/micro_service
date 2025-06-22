package com.naveen.micro_service.dto;

import lombok.Data;

@Data
public class AssignDriverRequest {
    private Long bookingId;
    private Long driverId;
}
