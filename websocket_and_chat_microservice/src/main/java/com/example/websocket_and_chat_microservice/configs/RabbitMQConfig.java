package com.example.websocket_and_chat_microservice.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SYNC_EXCHANGE = "sync-exchange";
    public static final String WEBSOCKET_MONITOR_SYNC_QUEUE = "websocket-monitor-queue";
    public static final String MONITOR_UPDATE_ROUTING_KEY = "sync.monitor.generated";
    public static final String OVERCONSUMPTION_ALERT_QUEUE = "overconsumption-alert-queue";
    public static final String ALERT_ROUTING_KEY = "alert.overconsumption";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Queue websocketMonitorSyncQueue() {
        return new Queue(WEBSOCKET_MONITOR_SYNC_QUEUE, true);
    }

    @Bean
    public Binding websocketMonitorSyncBinding(Queue websocketMonitorSyncQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(websocketMonitorSyncQueue)
                .to(syncExchange)
                .with(MONITOR_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Queue overconsumptionAlertQueue() {
        return new Queue(OVERCONSUMPTION_ALERT_QUEUE, true);
    }

    @Bean
    public Binding overconsumptionAlertBinding(Queue overconsumptionAlertQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(overconsumptionAlertQueue)
                .to(syncExchange)
                .with(ALERT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}