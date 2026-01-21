package com.example.monitoring_microservice.services.rabbitMQ;

import com.example.monitoring_microservice.configs.RabbitMQConfig;
import com.example.monitoring_microservice.dtos.rabbitMQ.OverconsumptionAlert;
import com.example.monitoring_microservice.dtos.rabbitMQ.SimulatorMessage;
import com.example.monitoring_microservice.entities.Device;
import com.example.monitoring_microservice.entities.EnergyReading;
import com.example.monitoring_microservice.repositories.DeviceRepository;
import com.example.monitoring_microservice.repositories.EnergyReadingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimulatorEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SimulatorEventConsumer.class);
    private final EnergyReadingRepository energyReadingRepository;
    private final DeviceRepository deviceRepository;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public SimulatorEventConsumer(EnergyReadingRepository energyReadingRepository,
                                   DeviceRepository deviceRepository,
                                   RabbitTemplate rabbitTemplate,
                                   SimpMessagingTemplate messagingTemplate) {
        this.energyReadingRepository = energyReadingRepository;
        this.deviceRepository = deviceRepository;
        this.rabbitTemplate = rabbitTemplate;
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
            logger.info("Saved energy reading: deviceId={}, timestamp={}, value={}", savedReading.getDeviceId(), savedReading.getTimestamp(), savedReading.getConsumptionValue());

            checkOverconsumption(message);

        } catch (Exception e) {
            logger.error("Error processing simulator message: {}", e.getMessage(), e);
        }
    }

    private void checkOverconsumption(SimulatorMessage message) {
        try {
            Optional<Device> deviceOpt = deviceRepository.findById(message.getDeviceId());
            
            if (deviceOpt.isEmpty()) {
                logger.warn("Device not found for overconsumption check: {}", message.getDeviceId());
                return;
            }

            Device device = deviceOpt.get();
            int maxConsVal = device.getMaxConsVal();
            double actualConsumption = message.getValue();

            if (actualConsumption > maxConsVal) {
                OverconsumptionAlert alert = new OverconsumptionAlert(
                    message.getDeviceId(),
                    message.getTimestamp(),
                    actualConsumption,
                    maxConsVal
                );

                rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SYNC_EXCHANGE,
                    RabbitMQConfig.ALERT_ROUTING_KEY,
                    alert
                );

                logger.warn("OVERCONSUMPTION DETECTED! Device: {}, Actual: {}, Threshold: {}", message.getDeviceId(), actualConsumption, maxConsVal);
            }
        } catch (Exception e) {
            logger.error("Error checking overconsumption: {}", e.getMessage(), e);
        }
    }
}
