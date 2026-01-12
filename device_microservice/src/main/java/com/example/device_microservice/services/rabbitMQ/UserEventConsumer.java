package com.example.device_microservice.services.rabbitMQ;

import com.example.device_microservice.configs.RabbitMQConfig;
import com.example.device_microservice.dtos.rabbitMQ.UserCreatedEvent;
import com.example.device_microservice.dtos.rabbitMQ.UserDeletedEvent;
import com.example.device_microservice.dtos.rabbitMQ.UserUpdatedEvent;
import com.example.device_microservice.entities.User;
import com.example.device_microservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    private final UserRepository userRepository;

    public UserEventConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_USER_SYNC_QUEUE)
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        logger.info("Received user.created event: {}", event);

        try {
            User user = new User(event.getUserId(), event.getUsername());

            userRepository.save(user);

            logger.info("User created successfully via event: ID={}, Username={}", event.getUserId(),
                    event.getUsername());

        } catch (Exception e) {
            logger.error("Error processing user.created event: {}", event, e);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_USER_DELETE_QUEUE)
    public void consumeUserDeletedEvent(UserDeletedEvent event) {
        logger.info("Received user.deleted event: {}", event);

        try {
            userRepository.deleteById(event.getUserId());
            logger.info("User deleted successfully via event: ID={}", event.getUserId());

        } catch (Exception e) {
            logger.error("Error processing user.deleted event: {}", event, e);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_USER_UPDATE_QUEUE)
    public void consumeUserUpdatedEvent(UserUpdatedEvent event) {
        logger.info("Received user.updated event: {}", event);
        try {
            Optional<User> optionalUser = userRepository.findById(event.getUserId());
            if (!optionalUser.isPresent()) {
                logger.error("User with id {} was not found", event.getUserId());
                throw new RuntimeException("User with id " + event.getUserId() + " was not found");
            }
            User user = optionalUser.get();
            user.setUsername(event.getUsername());
            userRepository.save(user);
            logger.info("User updated successfully via event: ID={}", event.getUserId());
        } catch (Exception e) {
            logger.error("Error processing user.updated event: {}", event, e);
            throw e;
        }
    }
}
