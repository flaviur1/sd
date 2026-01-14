package com.example.monitoring_microservice.services.rabbitMQ;

import com.example.monitoring_microservice.configs.RabbitMQConfig;
import com.example.monitoring_microservice.dtos.rabbitMQ.SimulatorMessage;
import com.example.monitoring_microservice.entities.EnergyReading;
import com.example.monitoring_microservice.repositories.EnergyReadingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SimulatorEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SimulatorEventConsumer.class);
    private final EnergyReadingRepository energyReadingRepository;

    public SimulatorEventConsumer(EnergyReadingRepository energyReadingRepository) {
        this.energyReadingRepository = energyReadingRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.MONITOR_SIMULATOR_QUEUE)
    public void consumeSimulatorMessage(String event) {
        logger.info("Received simulator message: {}", event);
    }
}
