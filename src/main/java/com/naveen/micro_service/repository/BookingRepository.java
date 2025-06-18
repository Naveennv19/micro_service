package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findTop3ByCustomerOrderByCreatedAtDesc(User customer);
    long countByCustomer(User customer);

    List<Booking> findAllByCustomerId(Long customerId);
}
