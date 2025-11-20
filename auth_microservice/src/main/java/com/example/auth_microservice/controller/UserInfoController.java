package com.example.auth_microservice.controller;


import com.example.auth_microservice.entity.AuthRequest;
import com.example.auth_microservice.entity.RegisterRequest;
import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.service.JwtService;
import com.example.auth_microservice.service.UserInfoDetails;
import com.example.auth_microservice.service.UserInfoService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;

    public UserInfoController(UserInfoService userInfoService, JwtService jwtService, AuthenticationManager authenticationManager, RestTemplateBuilder restTemplateBuilder) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("/registerByForm")
    public ResponseEntity<String> registerByForm(@RequestBody RegisterRequest registerRequest) {
        UUID id = UUID.randomUUID();
        String roles = "ROLE_USER";
        UserInfo userInfo = new UserInfo(id, registerRequest.getUsername(), registerRequest.getPassword(), roles);
        userInfoService.addUser(userInfo);
        try {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", id);
            userProfile.put("name", registerRequest.getUsername());
            userProfile.put("address", registerRequest.getAddress());
            userProfile.put("age", registerRequest.getAge());

            restTemplate.postForEntity("http://user-service:8080/users/form", userProfile, Void.class);

        } catch (Exception e) {
            userInfoService.deleteUser(userInfo.getUsername());
            return ResponseEntity.status(500).body("Registration failed: Could not create user profile.");
        }

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/registerByAdmin")
    public ResponseEntity<String> registerByAdmin(@RequestBody RegisterRequest registerRequest) {
        UUID id = UUID.randomUUID();
        String roles = registerRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = "ROLE_USER";
        }
        UserInfo userInfo = new UserInfo(id, registerRequest.getUsername(), registerRequest.getPassword(), roles);
        userInfoService.addUser(userInfo);
        try {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", id);
            userProfile.put("name", registerRequest.getUsername());
            userProfile.put("address", registerRequest.getAddress());
            userProfile.put("age", registerRequest.getAge());

            restTemplate.postForEntity("http://user-service:8080/users/byAdmin", userProfile, Void.class);
        } catch (Exception e) {
            userInfoService.deleteUser(userInfo.getUsername());
            return ResponseEntity.status(500).body("Registration failed: Could not create user profile.");
        }

        return ResponseEntity.ok("User created successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAndGenerateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername(), roles, userDetails.getId()));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = header.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userInfoService.deleteUser(username));

    }
}
