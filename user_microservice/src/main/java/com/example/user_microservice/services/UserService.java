package com.example.user_microservice.services;


import com.example.user_microservice.dtos.UserDTO;
import com.example.user_microservice.dtos.UserDetailsDTO;
import com.example.user_microservice.dtos.builders.UserBuilder;
import com.example.user_microservice.entities.User;
import com.example.user_microservice.handlers.exceptions.model.ResourceNotFoundException;
import com.example.user_microservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplateBuilder restTemplateBuilder) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDetailsDTO findUserById(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDetailsDTO(prosumerOptional.get());
    }

    public UUID insert(UserDetailsDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public UserDetailsDTO putUserById(UUID id, UserDetailsDTO user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User with id {} does not exist", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User u = optionalUser.get();
        u.setId(id);
        u.setAddress(user.getAddress());
        u.setAge(user.getAge());
        u.setName(user.getName());
        userRepository.save(u);
        return UserBuilder.toUserDetailsDTO(u);
    }

    public String deleteById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User with id {} does not exist", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        String username = optionalUser.get().getName();
        userRepository.deleteById(id);
        try {
            restTemplate.delete("http://auth-service:8080/auth/delete/" + username);
        } catch (Exception e) {
            LOGGER.error("User with username {} does not exist in auth DB", username);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with username: " + username);
        }
        return "User with id " + id + " has been succesfully deleted";
    }
}
