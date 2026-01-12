package com.example.user_microservice.services.rabbitMQ;

import com.example.user_microservice.configs.RabbitMQConfig;
import com.example.user_microservice.dtos.rabbitMQ.UserUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserUpdated(UUID userId, String username, String address, Integer age) {
        UserUpdatedEvent event = new UserUpdatedEvent(userId, username, address, age);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.USER_UPDATED_ROUTING_KEY,
                    event);
            logger.info("Published user.updated event for user: {} with ID: {}", username, userId);
        } catch (Exception e) {
            logger.error("Failed to publish user.updated event for user: {}", username, e);
        }
    }
}