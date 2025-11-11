package com.example.auth_microservice.controller;


import com.example.auth_microservice.entity.AuthRequest;
import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.service.JwtService;
import com.example.auth_microservice.service.UserInfoService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserInfoController {

    private UserInfoService service;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    public UserInfoController(UserInfoService service, JwtService jwtService, AuthenticationManagerBuilder authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        try {
            this.authenticationManager = authenticationManager.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    // Removed the role checks here as they are already managed in SecurityConfig

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

/*    @PostMapping("/validateToken")
    public ResponseEntity<> validateToken() {

    }*/
}
