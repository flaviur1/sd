package com.example.device_microservice.services.rabbitMQ;

import com.example.device_microservice.configs.RabbitMQConfig;
import com.example.device_microservice.dtos.rabbitMQ.DeviceCreatedEvent;
import com.example.device_microservice.dtos.rabbitMQ.DeviceDeletedEvent;
import com.example.device_microservice.dtos.rabbitMQ.DeviceUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(DeviceCreatedEvent.class);
    private final RabbitTemplate rabbitTemplate;

    public DeviceEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDeviceCreated(UUID deviceId, int maxConsVal, UUID userId) {
        DeviceCreatedEvent event = new DeviceCreatedEvent(deviceId, maxConsVal, userId);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.DEVICE_CREATED_ROUTING_KEY,
                    event);
            logger.info("Published device.created event for device with ID: {}", deviceId);
        } catch (Exception e) {
            logger.error("Failed to publish device.created event for device with ID: {}", deviceId, e);
        }
    }

    public void publishDeviceDeleted(UUID deviceId) {
        DeviceDeletedEvent event = new DeviceDeletedEvent(deviceId);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.DEVICE_DELETED_ROUTING_KEY,
                    event);
            logger.info("Published device.deleted event for device with ID: {}", deviceId);
        } catch (Exception e) {
            logger.error("Failed to publish device.deleted event for device with ID: {}", deviceId, e);
        }
    }

    public void publishDeviceUpdated(UUID deviceId, int maxConsVal, UUID userId) {
        DeviceUpdatedEvent event = new DeviceUpdatedEvent(deviceId, maxConsVal, userId);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.DEVICE_UPDATED_ROUTING_KEY,
                    event
            );
            logger.info("Published device.updated event for device with ID: {}", deviceId);
        } catch (Exception e) {
            logger.error("Failed to publish device.updated event for device with ID: {}", deviceId, e);
        }
    }
}