package com.example.auth_microservice.controller;


import com.example.auth_microservice.entity.AuthRequest;
import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.service.JwtService;
import com.example.auth_microservice.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserInfoController {

    private UserInfoService service;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    public UserInfoController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(service.addUser(userInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
