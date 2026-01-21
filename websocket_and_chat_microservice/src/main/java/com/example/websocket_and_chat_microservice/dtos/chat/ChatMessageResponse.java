package com.example.websocket_and_chat_microservice.dtos.chat;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private String message;
    private boolean isAdmin;
    private LocalDateTime timestamp;
    private String userId;
    
    public ChatMessageResponse() {
    }
    
    public ChatMessageResponse(String message, boolean isAdmin, LocalDateTime timestamp, String userId) {
        this.message = message;
        this.isAdmin = isAdmin;
        this.timestamp = timestamp;
        this.userId = userId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
