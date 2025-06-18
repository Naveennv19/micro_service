package com.naveen.micro_service.service;

import com.naveen.micro_service.dto.UserDto;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void registerUser(UserDto userDto) {
        // You may also add validations (e.g., email uniqueness) here

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword()) // make sure to hash in real app
                .phone(userDto.getPhone())
                .role(userDto.getRole() != null ? userDto.getRole() : UserRole.CUSTOMER)
                .build();

        userRepository.save(user);
    }
}
