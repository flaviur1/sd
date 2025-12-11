package com.example.device_microservice.controllers;

import com.example.device_microservice.dtos.UserDTO;
import com.example.device_microservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/device-user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/addByForm")
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {
        UUID id = userService.insert(userDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/addByAdmin")
    public ResponseEntity<Void> addUserByAdmin(@RequestBody UserDTO userDTO) {
        UUID id = userService.insert(userDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> exists(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateUsername(id, userDTO);
        return ResponseEntity.ok(updated);
    }

}
