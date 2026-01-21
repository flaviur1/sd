package com.example.websocket_and_chat_microservice.dtos.chat;

public class ChatMessageRequest {
    private String message;
    private String userId;
    
    public ChatMessageRequest() {
    }
    
    public ChatMessageRequest(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
