package com.example.user_microservice.controllers;

import com.example.user_microservice.dtos.UserDTO;
import com.example.user_microservice.dtos.UserDetailsDTO;
import com.example.user_microservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.findUsers());
    }

    @PostMapping("/form")
    public ResponseEntity<Void> createByForm(@Valid @RequestBody UserDetailsDTO user) {
        UUID id = userService.insert(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build(); // 201 + Location header
    }

    @PostMapping("/byAdmin")
    public ResponseEntity<Void> createByAdmin(@Valid @RequestBody UserDetailsDTO user) {
        UUID id = userService.insert(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> putUserById(@PathVariable UUID id, @RequestBody UserDetailsDTO user) {
        return ResponseEntity.ok(userService.putUserById(id, user));
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable UUID id) {
        return userService.deleteById(id);
    }

    @PutMapping("/makeAdmin/{id}")
    public ResponseEntity<String> makeUserAdmin(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.makeUserAdmin(id));
    }
}
