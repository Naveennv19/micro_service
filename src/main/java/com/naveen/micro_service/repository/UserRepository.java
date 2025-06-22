package com.naveen.micro_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.naveen.micro_service.model.User;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByRole(User.UserRole role);

List<User> findByRoleAndStatus(User.UserRole role, User.UserStatus status);

}
