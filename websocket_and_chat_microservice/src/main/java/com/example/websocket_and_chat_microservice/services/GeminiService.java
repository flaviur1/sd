package com.example.websocket_and_chat_microservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateResponse(String message) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", message)
                            ))
                    )
            );

            String fullUrl = apiUrl + "?key=" + apiKey;
            System.out.println("Gemini API URL: " + apiUrl);
            System.out.println("API Key present: " + (apiKey != null && !apiKey.isEmpty()));

            Map<String, Object> response = webClient.post()
                    .uri(fullUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        return (String) parts.get(0).get("text");
                    }
                }
            }

            System.out.println("Gemini response was empty or malformed: " + response);
            return "I couldn't generate a response. Please try again.";

        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            return "I'm having trouble connecting. Please try again later.";
        }
    }
}
