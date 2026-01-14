package com.example.monitoring_microservice.services.rabbitMQ;

import com.example.monitoring_microservice.configs.RabbitMQConfig;
import com.example.monitoring_microservice.dtos.rabbitMQ.DeviceCreatedEvent;
import com.example.monitoring_microservice.dtos.rabbitMQ.DeviceDeletedEvent;
import com.example.monitoring_microservice.dtos.rabbitMQ.DeviceUpdatedEvent;
import com.example.monitoring_microservice.entities.Device;
import com.example.monitoring_microservice.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeviceEventConsumer.class);
    private final DeviceRepository deviceRepository;

    public DeviceEventConsumer(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.MONITOR_DEVICE_SYNC_QUEUE)
    public void consumeDeviceCreated(DeviceCreatedEvent event) {
        logger.info("Received device.created event: {}", event);

        try {
            Device device = new Device();
            device.setId(event.getDeviceId());
            device.setMaxConsVal(event.getMaxConsVal());
            device.setUserId(event.getUserId());

            deviceRepository.save(device);

            logger.info("Device successfully added via event: ID={}", event.getDeviceId());

        } catch (Exception e) {
            logger.error("Error processing device.created event: {}", event, e);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConfig.MONITOR_DEVICE_DELETE_QUEUE)
    public void consumeDeviceDeleted(DeviceDeletedEvent event) {
        logger.info("Received device.deleted event: {}", event);

        try {
            deviceRepository.deleteById(event.getDeviceId());
            logger.info("Device deleted successfully via event: ID={}", event.getDeviceId());

        } catch (Exception e) {
            logger.error("Error processing device.deleted event: {}", event, e);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConfig.MONITOR_DEVICE_UPDATE_QUEUE)
    public void consumeDeviceUpdate(DeviceUpdatedEvent event) {
        logger.info("Received device.updated event: {}", event);

        try {
            Optional<Device> deviceOptional = deviceRepository.findById(event.getDeviceId());
            if (!deviceOptional.isPresent()) {
                logger.error("Device with id {} was not found", event.getDeviceId());
                throw new RuntimeException("Device with id " + event.getDeviceId() + " was not found");
            }
            Device d = deviceOptional.get();
            d.setId(event.getDeviceId());
            d.setMaxConsVal(event.getMaxConsVal());
            d.setUserId(event.getUserId());
            deviceRepository.save(d);

            logger.info("Device updated successfully via event: ID={}", event.getDeviceId());

        } catch (Exception e) {
            logger.error("Error processing device.updated event: {}", event, e);
            throw e;
        }
    }
}
