package com.example.websocket_and_chat_microservice.services;

import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final GeminiService geminiService;

    public ChatbotService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String processMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please enter a message.";
        }
        String lowerMessage = message.toLowerCase().trim();
        for (String[] faq : FAQData.FAQ_LIST) {
            String[] keywords = faq[0].split(",");

            for (String keyword : keywords) {
                if (lowerMessage.contains(keyword.trim().toLowerCase())) {
                    return faq[1];
                }
            }
        }

        return geminiService.generateResponse(message);
    }
}
