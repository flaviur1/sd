package com.example.websocket_and_chat_microservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_session")
public class ChatSession {
    
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID userId;
    
    @Column(nullable = false)
    private boolean adminActive = false;
    
    @Column
    private UUID adminId;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    public ChatSession() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ChatSession(UUID userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.adminActive = false;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public boolean isAdminActive() {
        return adminActive;
    }
    
    public void setAdminActive(boolean adminActive) {
        this.adminActive = adminActive;
    }
    
    public UUID getAdminId() {
        return adminId;
    }
    
    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
