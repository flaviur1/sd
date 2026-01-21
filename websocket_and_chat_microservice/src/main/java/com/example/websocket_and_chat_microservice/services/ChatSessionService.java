package com.example.websocket_and_chat_microservice.services;

import com.example.websocket_and_chat_microservice.entities.ChatSession;
import com.example.websocket_and_chat_microservice.repositories.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatSessionService {
    
    private ChatSessionRepository chatSessionRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository){
        this.chatSessionRepository = chatSessionRepository;
    }
    
    public ChatSession getOrCreateSession(UUID userId) {
        return chatSessionRepository.findByUserId(userId)
            .orElseGet(() -> {
                ChatSession newSession = new ChatSession(userId);
                return chatSessionRepository.save(newSession);
            });
    }
    
    public boolean isAdminActive(UUID userId) {
        return chatSessionRepository.findByUserId(userId)
            .map(ChatSession::isAdminActive)
            .orElse(false);
    }
}
