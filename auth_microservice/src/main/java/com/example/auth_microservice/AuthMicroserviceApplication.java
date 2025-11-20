package com.example.auth_microservice;

import com.example.auth_microservice.entity.UserInfo;
import com.example.auth_microservice.repository.UserInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class AuthMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthMicroserviceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserInfoRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByUsername("admin").isEmpty()) {
                UserInfo admin = new UserInfo();
                admin.setId(UUID.randomUUID());
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRoles("ROLE_ADMIN");

                repository.save(admin);
                System.out.println("ADMIN ACCOUNT CREATED: Username: admin | Password: admin");
            }
        };
    }
}