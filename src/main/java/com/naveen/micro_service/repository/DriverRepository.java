package com.naveen.micro_service.repository;

import com.naveen.micro_service.model.DriverDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverDetails, Long> {
}
