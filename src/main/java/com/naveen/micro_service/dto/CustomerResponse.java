package com.naveen.micro_service.dto;

import com.naveen.micro_service.model.Customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {

    private Long id;
    private String name;
    private String mobile;
    private Long userId;
    private String userEmail;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.userId = customer.getUser().getId();
        this.userEmail = customer.getUser().getEmail(); // ✅ assignment was missing
    }
}
