package com.naveen.micro_service.dto;

import com.naveen.micro_service.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private User.UserRole role;

    // Only used if role == DRIVER
    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleType;
    private Integer yearsOfExperience;
}
