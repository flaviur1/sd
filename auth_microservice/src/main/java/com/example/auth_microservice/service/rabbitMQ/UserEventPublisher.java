package com.example.auth_microservice.service.rabbitMQ;

import com.example.auth_microservice.config.RabbitMQConfig;
import com.example.auth_microservice.entity.rabbitMQ.UserCreatedEvent;
import com.example.auth_microservice.entity.rabbitMQ.UserDeletedEvent;
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

    public void publishUserCreated(UUID userId, String username, String address, Integer age) {
        UserCreatedEvent event = new UserCreatedEvent(userId, username, address, age);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                    event);
            logger.info("Published user.created event for user: {} with ID: {}", username, userId);
        } catch (Exception e) {
            logger.error("Failed to publish user.created event for user: {}", username, e);
        }
    }

    public void publishUserDeleted(UUID userId) {
        UserDeletedEvent event = new UserDeletedEvent(userId);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.USER_DELETED_ROUTING_KEY,
                    event);
            logger.info("Published user.deleted event for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to publish user.deleted event for user ID: {}", userId, e);
        }
    }
}
