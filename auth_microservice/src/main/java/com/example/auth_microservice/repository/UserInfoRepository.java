package com.example.auth_microservice.repository;

import com.example.auth_microservice.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findById(UUID id);

    void deleteById(UUID id);
}