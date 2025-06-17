package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByCustomerId(Long customerId);
    List<Booking> findTop3ByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
