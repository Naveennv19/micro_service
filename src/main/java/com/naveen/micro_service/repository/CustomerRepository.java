package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(@Param("userId") Long userId); // <-- make sure this is Optional
}
