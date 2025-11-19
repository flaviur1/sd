package com.example.auth_microservice.controller;


import com.example.auth_microservice.entity.AuthRequest;
import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.service.JwtService;
import com.example.auth_microservice.service.UserInfoDetails;
import com.example.auth_microservice.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public UserInfoController(UserInfoService userInfoService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userInfoService.addUser(userInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAndGenerateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername()));
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
}
