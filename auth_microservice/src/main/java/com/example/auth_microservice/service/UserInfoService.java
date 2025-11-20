package com.example.auth_microservice.service;

import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, @Lazy PasswordEncoder encoder) {
        this.userInfoRepository = userInfoRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);

        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new UserInfoDetails(userInfo.get());
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "User added successfully!";
    }
    @Transactional
    public String deleteUser(String username) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findByUsername(username);
        if (!optionalUserInfo.isPresent()) {
            return "User with username " + username + " was not found";
        }
        userInfoRepository.deleteByUsername(username);
        return "User with id " + username + " has been succesfully deleted";
    }
}