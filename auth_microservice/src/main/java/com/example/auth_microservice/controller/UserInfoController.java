package com.example.auth_microservice.controller;

import com.example.auth_microservice.entity.AuthRequest;
import com.example.auth_microservice.entity.RegisterRequest;
import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.service.JwtService;
import com.example.auth_microservice.service.UserEventPublisher;
import com.example.auth_microservice.service.UserInfoDetails;
import com.example.auth_microservice.service.UserInfoService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final UserEventPublisher userEventPublisher;

    public UserInfoController(UserInfoService userInfoService, JwtService jwtService,
            AuthenticationManager authenticationManager, RestTemplateBuilder restTemplateBuilder,
            UserEventPublisher userEventPublisher) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.restTemplate = restTemplateBuilder.build();
        this.userEventPublisher = userEventPublisher;
    }

    @PostMapping("/registerByForm")
    public ResponseEntity<String> registerByForm(@RequestBody RegisterRequest registerRequest) {
        UUID id = UUID.randomUUID();
        String roles = "ROLE_USER";
        UserInfo userInfo = new UserInfo(id, registerRequest.getUsername(), registerRequest.getPassword(), roles);
        userInfoService.addUser(userInfo);
        // try {
        // Map<String, Object> userProfile = new HashMap<>();
        // userProfile.put("id", id);
        // userProfile.put("name", registerRequest.getUsername());
        // userProfile.put("address", registerRequest.getAddress());
        // userProfile.put("age", registerRequest.getAge());

        // restTemplate.postForEntity("http://user-service:8080/users/form",
        // userProfile, Void.class);

        // } catch (Exception e) {
        // userInfoService.deleteUser(userInfo.getId());
        // return ResponseEntity.status(500).body("Registration failed: Could not create
        // user profile.");
        // }
        userEventPublisher.publishUserCreated(id, registerRequest.getUsername(),
                registerRequest.getAddress(), registerRequest.getAge());

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/registerByAdmin")
    public ResponseEntity<String> registerByAdmin(@RequestBody RegisterRequest registerRequest,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtService.extractUserId(token);
        String userRoles = jwtService.extractRoles(token);

        UUID id = UUID.randomUUID();
        String roles = registerRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = "ROLE_USER";
        }
        UserInfo userInfo = new UserInfo(id, registerRequest.getUsername(), registerRequest.getPassword(), roles);
        userInfoService.addUser(userInfo);
        // try {
        // Map<String, Object> userProfile = new HashMap<>();
        // userProfile.put("id", id);
        // userProfile.put("name", registerRequest.getUsername());
        // userProfile.put("address", registerRequest.getAddress());
        // userProfile.put("age", registerRequest.getAge());

        // HttpHeaders headers = new HttpHeaders();
        // headers.set("UserId", userId);
        // headers.set("UserRoles", userRoles);

        // HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(userProfile,
        // headers);

        // restTemplate.postForEntity("http://user-service:8080/users/byAdmin",
        // requestEntity, Void.class);
        // } catch (Exception e) {
        // userInfoService.deleteUser(userInfo.getId());
        // return ResponseEntity.status(500).body("Registration failed: Could not create
        // user profile.");
        // }

        userEventPublisher.publishUserCreated(id, registerRequest.getUsername(),
                registerRequest.getAddress(), registerRequest.getAge());

        return ResponseEntity.ok("User created successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAndGenerateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
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

    @RequestMapping(value = "/validate", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.DELETE })
    public ResponseEntity<Void> validate(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = header.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                // Extract claims and return as headers for Traefik ForwardAuth
                String roles = jwtService.extractRoles(token);
                String userId = jwtService.extractUserId(token);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("UserId", userId);
                responseHeaders.set("UserRoles", roles != null ? roles : "");

                return ResponseEntity.ok().headers(responseHeaders).build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteByUsername(@PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtService.extractUserId(token);
        String userRoles = jwtService.extractRoles(token);

        // try {
        // HttpHeaders headers = new HttpHeaders();
        // headers.set("UserId", userId);
        // headers.set("UserRoles", userRoles);
        // HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // restTemplate.exchange(
        // "http://user-service:8080/users/" + id,
        // HttpMethod.DELETE,
        // requestEntity,
        // Void.class);
        // userInfoService.deleteUser(id);
        // return ResponseEntity.ok("User deleted successfully from all databases.");
        // } catch (Exception e) {
        // return ResponseEntity.status(500).body("Error deleting user: " +
        // e.getMessage());
        // }

        userEventPublisher.publishUserDeleted(id);
        userInfoService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully from all databases.");
    }

    @PutMapping("/admin/makeAdmin/{id}")
    public ResponseEntity<String> makeUserAdmin(@PathVariable UUID id) {
        try {
            String message = userInfoService.makeUserAdmin(id);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
