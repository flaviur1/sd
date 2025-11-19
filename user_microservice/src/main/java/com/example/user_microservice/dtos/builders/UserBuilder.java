package com.example.user_microservice.dtos.builders;

import com.example.user_microservice.dtos.UserDTO;
import com.example.user_microservice.dtos.UserDetailsDTO;
import com.example.user_microservice.entities.User;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getAge());
    }

    public static UserDetailsDTO toUserDetailsDTO(User user) {
        return new UserDetailsDTO(user.getId(), user.getName(), user.getAddress(), user.getAge());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO) {
        return new User(userDetailsDTO.getId(),
                userDetailsDTO.getName(),
                userDetailsDTO.getAddress(),
                userDetailsDTO.getAge());
    }
}
