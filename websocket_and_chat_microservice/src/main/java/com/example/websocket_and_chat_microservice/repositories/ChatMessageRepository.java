package com.example.websocket_and_chat_microservice.repositories;

import com.example.websocket_and_chat_microservice.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(UUID sessionId);
    ChatMessage findTopBySessionIdOrderByTimestampDesc(UUID sessionId);
}
