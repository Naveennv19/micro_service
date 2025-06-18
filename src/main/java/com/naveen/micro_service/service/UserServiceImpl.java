package com.naveen.micro_service.service;

import com.naveen.micro_service.dto.UserDto;
import com.naveen.micro_service.model.Customer;
import com.naveen.micro_service.model.DriverDetails;
import com.naveen.micro_service.model.User;
import com.naveen.micro_service.model.User.UserRole;
import com.naveen.micro_service.repository.CustomerRepository;
import com.naveen.micro_service.repository.DriverRepository;
import com.naveen.micro_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    @Override
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());

        UserRole role = userDto.getRole() != null ? userDto.getRole() : UserRole.CUSTOMER;
        user.setRole(role);

        userRepository.save(user);

        if (role == UserRole.CUSTOMER) {
            Customer customer = new Customer();
            customer.setUser(user);
            customerRepository.save(customer);
        } else if (role == UserRole.DRIVER) {
            DriverDetails driverDetails = new DriverDetails();
            driverDetails.setUser(user);
            driverRepository.save(driverDetails);
        }
    }
}
