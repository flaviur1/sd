package com.example.user_microservice.configs;

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
    public static final String USER_SYNC_QUEUE = "user-sync-queue";
    public static final String USER_DELETE_QUEUE = "user-delete-queue";
    public static final String USER_CREATED_ROUTING_KEY = "sync.user.created";
    public static final String USER_DELETED_ROUTING_KEY = "sync.user.deleted";
    public static final String USER_UPDATED_ROUTING_KEY = "sync.user.updated";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Queue userSyncQueue() {
        return new Queue(USER_SYNC_QUEUE, true);
    }

    @Bean
    public Binding userSyncBinding(Queue userSyncQueue, TopicExchange syncExchange) {
        return BindingBuilder
                .bind(userSyncQueue)
                .to(syncExchange)
                .with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue userDeleteQueue() {
        return new Queue(USER_DELETE_QUEUE, true);
    }

    @Bean
    public Binding userDeleteBinding(Queue userDeleteQueue, TopicExchange syncExchange) {
        return BindingBuilder
                .bind(userDeleteQueue)
                .to(syncExchange)
                .with(USER_DELETED_ROUTING_KEY);
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
