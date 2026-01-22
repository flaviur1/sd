package com.example.websocket_and_chat_microservice.controllers;

import com.example.websocket_and_chat_microservice.dtos.chat.AdminChatRequest;
import com.example.websocket_and_chat_microservice.dtos.chat.ChatMessageRequest;
import com.example.websocket_and_chat_microservice.dtos.chat.ChatMessageResponse;
import com.example.websocket_and_chat_microservice.dtos.chat.ChatSessionDTO;
import com.example.websocket_and_chat_microservice.entities.ChatMessage;
import com.example.websocket_and_chat_microservice.entities.ChatSession;
import com.example.websocket_and_chat_microservice.repositories.ChatMessageRepository;
import com.example.websocket_and_chat_microservice.services.ChatSessionService;
import com.example.websocket_and_chat_microservice.services.ChatbotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ChatController {
    
    private final ChatbotService chatbotService;
    private final ChatSessionService chatSessionService;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    public ChatController(ChatbotService chatbotService,
                         ChatSessionService chatSessionService,
                         ChatMessageRepository chatMessageRepository,
                         SimpMessagingTemplate messagingTemplate) {
        this.chatbotService = chatbotService;
        this.chatSessionService = chatSessionService;
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/chat/send")
    public void handleUserMessage(ChatMessageRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            return;
        }
        UUID userId = UUID.fromString(request.getUserId());
        ChatSession session = chatSessionService.getOrCreateSession(userId);

        ChatMessage userMessage = new ChatMessage(session.getId(), request.getMessage(), "USER");
        chatMessageRepository.save(userMessage);

        if (request.getMessage().trim().equalsIgnoreCase("CONNECT")) {
            session.setAdminActive(true);
            chatSessionService.saveSession(session);
            
            ChatSessionDTO sessionDTO = new ChatSessionDTO(
                session.getId().toString(),
                userId.toString(),
                session.getCreatedAt(),
                request.getMessage()
            );
            messagingTemplate.convertAndSend("/topic/admin/new-session", sessionDTO);
            
            ChatMessageResponse response = new ChatMessageResponse(
                "Connecting you with an admin. Please wait...",
                false,
                LocalDateTime.now(),
                userId.toString()
            );
            messagingTemplate.convertAndSend("/topic/chat-responses", response);
            return;
        }

        if (session.isAdminActive()) {
            messagingTemplate.convertAndSend("/topic/admin/messages/" + session.getId(), userMessage);
            return;
        }

        String botResponseText = chatbotService.processMessage(request.getMessage());
        ChatMessage botMessage = new ChatMessage(session.getId(), botResponseText, "BOT");
        chatMessageRepository.save(botMessage);

        ChatMessageResponse response = new ChatMessageResponse(
            botResponseText,
            false,
            LocalDateTime.now(),
            userId.toString()
        );
        
        messagingTemplate.convertAndSend("/topic/chat-responses", response);
    }
    
    @MessageMapping("/chat/admin/send")
    public void handleAdminMessage(AdminChatRequest request) {
        UUID sessionId = UUID.fromString(request.getSessionId());
        ChatSession session = chatSessionService.getSessionById(sessionId).orElse(null);
        
        if (session == null) {
            return;
        }
        
        ChatMessage adminMessage = new ChatMessage(sessionId, request.getMessage(), "ADMIN");
        chatMessageRepository.save(adminMessage);
        
        ChatMessageResponse response = new ChatMessageResponse(
            request.getMessage(),
            true,
            LocalDateTime.now(),
            session.getUserId().toString()
        );
        
        messagingTemplate.convertAndSend("/topic/chat-responses", response);
    }
    
    @GetMapping("/chat/sessions")
    public List<ChatSessionDTO> getActiveSessions() {
        return chatSessionService.getActiveSessions().stream()
            .map(session -> {
                ChatMessage lastMessage = chatMessageRepository.findTopBySessionIdOrderByTimestampDesc(session.getId());
                return new ChatSessionDTO(
                    session.getId().toString(),
                    session.getUserId().toString(),
                    session.getCreatedAt(),
                    lastMessage != null ? lastMessage.getMessage() : "No messages"
                );
            })
            .collect(Collectors.toList());
    }
    
    @GetMapping("/chat/messages/{sessionId}")
    public List<ChatMessage> getChatHistory(@PathVariable String sessionId) {
        UUID sessionUUID = UUID.fromString(sessionId);
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionUUID);
    }
}
