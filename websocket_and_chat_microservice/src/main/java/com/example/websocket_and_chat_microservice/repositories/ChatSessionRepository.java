package com.example.websocket_and_chat_microservice.repositories;

import com.example.websocket_and_chat_microservice.entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    Optional<ChatSession> findByUserId(UUID userId);
    List<ChatSession> findByAdminActiveTrue();
}
