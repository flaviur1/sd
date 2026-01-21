package com.example.websocket_and_chat_microservice.services.rabbitMQ;

import com.example.websocket_and_chat_microservice.configs.RabbitMQConfig;
import com.example.websocket_and_chat_microservice.dtos.rabbitMQ.SimulatorMessage;
import com.example.websocket_and_chat_microservice.dtos.websocket.EnergyReadingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MonitoringUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringUpdateConsumer.class);
    private final SimpMessagingTemplate messagingTemplate;

    public MonitoringUpdateConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.WEBSOCKET_MONITOR_SYNC_QUEUE)
    public void consumeSimulatorMessage(SimulatorMessage message) {
        try {
            logger.info("Received monitoring message: deviceId={}, value={}, timestamp={}", 
                message.getDeviceId(), message.getValue(), message.getTimestamp());

            // Convert SimulatorMessage to EnergyReadingEvent for WebSocket
            EnergyReadingEvent event = new EnergyReadingEvent(
                message.getDeviceId(),
                message.getTimestamp(),
                message.getValue(),
                message.getTimestamp().getHour()
            );

            messagingTemplate.convertAndSend("/topic/energy-readings", event);
            logger.info("Broadcasted energy reading to /topic/energy-readings: deviceId={}, hour={}", 
                event.getDeviceId(), event.getHour());

        } catch (Exception e) {
            logger.error("Error processing simulator message: {}", e.getMessage(), e);
        }
    }
}
