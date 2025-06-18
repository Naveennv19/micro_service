package com.naveen.micro_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.naveen.micro_service.model.User;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
