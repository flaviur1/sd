package com.example.websocket_and_chat_microservice.controllers;

import com.example.websocket_and_chat_microservice.dtos.chat.ChatMessageRequest;
import com.example.websocket_and_chat_microservice.dtos.chat.ChatMessageResponse;
import com.example.websocket_and_chat_microservice.entities.ChatMessage;
import com.example.websocket_and_chat_microservice.entities.ChatSession;
import com.example.websocket_and_chat_microservice.repositories.ChatMessageRepository;
import com.example.websocket_and_chat_microservice.services.ChatSessionService;
import com.example.websocket_and_chat_microservice.services.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
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

        if (session.isAdminActive()) {
            ChatMessageResponse response = new ChatMessageResponse(
                "Your message has been forwarded to an admin. They will respond shortly.",
                true,
                LocalDateTime.now(),
                userId.toString()
            );
            messagingTemplate.convertAndSend("/topic/chat-responses", response);
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
}
