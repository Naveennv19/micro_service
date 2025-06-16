
package com.naveen.micro_service.dto;


import lombok.*;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private Long userId;
    private int totalBookings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
