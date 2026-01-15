package com.example.monitoring_microservice.services.rabbitMQ;

import com.example.monitoring_microservice.configs.RabbitMQConfig;
import com.example.monitoring_microservice.dtos.rabbitMQ.SimulatorMessage;
import com.example.monitoring_microservice.entities.EnergyReading;
import com.example.monitoring_microservice.repositories.EnergyReadingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SimulatorEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SimulatorEventConsumer.class);
    private final EnergyReadingRepository energyReadingRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public SimulatorEventConsumer(EnergyReadingRepository energyReadingRepository, SimpMessagingTemplate messagingTemplate) {
        this.energyReadingRepository = energyReadingRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.MONITOR_SIMULATOR_QUEUE)
    public void consumeSimulatorMessage(SimulatorMessage message) {
        try {
            logger.info("Received simulator message: deviceId={}, value={}, timestamp={}", message.getDeviceId(), message.getValue(), message.getTimestamp());

            EnergyReading reading = new EnergyReading(
                    message.getDeviceId(),
                    message.getTimestamp(),
                    message.getValue()
            );

            EnergyReading savedReading = energyReadingRepository.save(reading);
            logger.info("Saved energy reading: deviceId={}, timestamp={}, value={}",savedReading.getDeviceId(), savedReading.getTimestamp(), savedReading.getConsumptionValue());

            messagingTemplate.convertAndSend("/topic/energy-readings", savedReading);
            logger.info("Broadcasted energy reading to /topic/energy-readings");

        } catch (Exception e) {
            logger.error("Error processing simulator message: {}", e.getMessage(), e);
        }
    }
}
