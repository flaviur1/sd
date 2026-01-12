package com.example.device_microservice.configs;

import java.util.function.BinaryOperator;

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
    public static final String DEVICE_USER_SYNC_QUEUE = "device-user-sync-queue";
    public static final String DEVICE_USER_DELETE_QUEUE = "device-user-delete-queue";
    public static final String DEVICE_USER_UPDATE_QUEUE = "device-user-update-queue";
    public static final String DEVICE_USER_CREATED_ROUTING_KEY = "sync.user.created";
    public static final String DEVICE_USER_DELETED_ROUTING_KEY = "sync.user.deleted";
    public static final String DEVICE_USER_UPDATED_ROUTING_KEY = "sync.user.updated";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Queue deviceSyncQueue() {
        return new Queue(DEVICE_USER_SYNC_QUEUE, true);
    }

    @Bean
    public Binding deviceSyncBinding(Queue deviceSyncQueue, TopicExchange syncExchange) {
        return BindingBuilder
                .bind(deviceSyncQueue)
                .to(syncExchange)
                .with(DEVICE_USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue deviceDeleteUserQueue() {
        return new Queue(DEVICE_USER_DELETE_QUEUE, true);
    }

    @Bean
    public Binding deviceDeleteUserBinding(Queue deviceDeleteUserQueue, TopicExchange syncExchange) {
        return BindingBuilder
                .bind(deviceDeleteUserQueue)
                .to(syncExchange)
                .with(DEVICE_USER_DELETED_ROUTING_KEY);
    }

    @Bean
    public Queue deviceUpdateUserQueue() {
        return new Queue(DEVICE_USER_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding deviceUpdateUserBinding(Queue deviceUpdateUserQueue, TopicExchange syncExchange) {
        return BindingBuilder
                .bind(deviceUpdateUserQueue)
                .to(syncExchange)
                .with(DEVICE_USER_UPDATED_ROUTING_KEY);
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
