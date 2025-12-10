package com.example.user_microservice.controllers;

import com.example.user_microservice.dtos.UserDTO;
import com.example.user_microservice.dtos.UserDetailsDTO;
import com.example.user_microservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    private final RestTemplate restTemplate;

    public UserController(UserService userService, RestTemplateBuilder restTemplateBuilder) {
        this.userService = userService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.findUsers());
    }

    @PostMapping("/form")
    public ResponseEntity<String> createByForm(@Valid @RequestBody UserDetailsDTO user) {
        userService.insert(user);
        try {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", user.getId());
            userProfile.put("username", user.getName());

            restTemplate.postForEntity("http://device-service:8080/device-user/addByForm", userProfile, Void.class);
        } catch (Exception e) {
            userService.deleteById(user.getId());
            return ResponseEntity.status(500).body("Registration failed: Could not create user profile (user-service).");
        }
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/byAdmin")
    public ResponseEntity<String> createByAdmin(@Valid @RequestBody UserDetailsDTO user, @RequestHeader("Authorization") String token) {
        userService.insert(user);
        try {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", user.getId());
            userProfile.put("username", user.getName());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(userProfile, headers);

            restTemplate.postForEntity("http://device-service:8080/device-user/addByAdmin", requestEntity, Void.class);
        } catch (Exception e) {
            userService.deleteById(user.getId());
            return ResponseEntity.status(500).body("Registration failed: Could not create user profile.");
        }
        return ResponseEntity.ok("User registered successfully!");
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
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id, @RequestHeader("Authorization") String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    "http://device-service:8080/device-user/" + id,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            );
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully from all databases.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
}
