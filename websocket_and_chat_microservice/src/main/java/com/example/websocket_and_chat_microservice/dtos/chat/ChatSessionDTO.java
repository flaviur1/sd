package com.example.websocket_and_chat_microservice.dtos.chat;

import java.time.LocalDateTime;

public class ChatSessionDTO {
    private String sessionId;
    private String userId;
    private LocalDateTime createdAt;
    private String lastMessage;

    public ChatSessionDTO() {
    }

    public ChatSessionDTO(String sessionId, String userId, LocalDateTime createdAt, String lastMessage) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.lastMessage = lastMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
