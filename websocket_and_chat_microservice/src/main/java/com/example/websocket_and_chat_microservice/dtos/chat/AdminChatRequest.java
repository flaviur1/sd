package com.example.websocket_and_chat_microservice.dtos.chat;

public class AdminChatRequest {
    private String sessionId;
    private String message;
    private String adminId;

    public AdminChatRequest() {
    }

    public AdminChatRequest(String sessionId, String message, String adminId) {
        this.sessionId = sessionId;
        this.message = message;
        this.adminId = adminId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
