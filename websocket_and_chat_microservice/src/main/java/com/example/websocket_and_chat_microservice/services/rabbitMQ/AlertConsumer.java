package com.example.websocket_and_chat_microservice.services.rabbitMQ;

import com.example.websocket_and_chat_microservice.configs.RabbitMQConfig;
import com.example.websocket_and_chat_microservice.dtos.rabbitMQ.OverconsumptionAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlertConsumer.class);
    private final SimpMessagingTemplate messagingTemplate;

    public AlertConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.OVERCONSUMPTION_ALERT_QUEUE)
    public void consumeAlert(OverconsumptionAlert alert) {
        try {
            logger.warn("Received overconsumption alert: deviceId={}, actual={}, threshold={}", alert.getDeviceId(), alert.getActualConsumption(), alert.getThreshold());

            messagingTemplate.convertAndSend("/topic/overconsumption-alerts", alert);
            logger.info("Broadcasted overconsumption alert to /topic/overconsumption-alerts");

        } catch (Exception e) {
            logger.error("Error processing overconsumption alert: {}", e.getMessage(), e);
        }
    }
}
