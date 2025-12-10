package com.example.device_microservice.services;

import com.example.device_microservice.dtos.UserDTO;
import com.example.device_microservice.dtos.builder.UserBuilder;
import com.example.device_microservice.entities.User;
import com.example.device_microservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID insert(UserDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in the database", user.getId());
        return user.getId();
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public UserDTO findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("User with id " + id + " was not found");
        }
        return UserBuilder.toUserDTO(user.get());
    }

    public String deleteById(UUID id) {
        userRepository.deleteById(id);
        return "User with id " + id + " has been deleted succesfully";
    }
}
