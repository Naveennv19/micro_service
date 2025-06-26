package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.Booking;
import com.naveen.micro_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(User customer);
    List<Booking> findByCustomerAndStatus(User customer, Booking.BookingStatus status);

    List<Booking> findByDriver(User driver);
    List<Booking> findByDriverAndStatus(User driver, Booking.BookingStatus status);

    List<Booking> findAllByCustomerId(Long customerId);
}
