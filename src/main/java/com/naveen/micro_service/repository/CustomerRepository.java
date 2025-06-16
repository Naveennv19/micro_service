package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(Long userId); // <-- make sure this is Optional
}
