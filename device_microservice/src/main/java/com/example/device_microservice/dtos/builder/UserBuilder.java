package com.example.device_microservice.dtos.builder;

import com.example.device_microservice.dtos.UserDTO;
import com.example.device_microservice.entities.User;

public class UserBuilder {
    private UserBuilder() {

    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername());
    }
}
