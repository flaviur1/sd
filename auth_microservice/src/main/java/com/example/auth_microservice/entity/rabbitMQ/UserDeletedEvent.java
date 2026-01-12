package com.example.auth_microservice.entity.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDeletedEvent {
    private UUID userId;
    private LocalDateTime timestamp;

    public UserDeletedEvent() {
    }

    public UserDeletedEvent(UUID userId) {
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
