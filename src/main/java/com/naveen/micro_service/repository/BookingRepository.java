package com.naveen.micro_service.repository;
import com.naveen.micro_service.model.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Customer, Long> {
    long countByCustomerId(Long customerId);
    List<Customer> findTop3ByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
