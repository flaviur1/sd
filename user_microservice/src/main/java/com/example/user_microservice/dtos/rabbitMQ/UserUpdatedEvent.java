package com.example.user_microservice.dtos.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserUpdatedEvent {
    private UUID userId;
    private String username;
    private String address;
    private Integer age;
    private LocalDateTime timestamp;

    public UserUpdatedEvent() {
    }

    public UserUpdatedEvent(UUID userId, String username, String address, Integer age) {
        this.userId = userId;
        this.username = username;
        this.address = address;
        this.age = age;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                ", timestamp=" + timestamp +
                '}';
    }
}
