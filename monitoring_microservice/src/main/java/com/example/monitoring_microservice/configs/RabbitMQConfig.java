package com.example.monitoring_microservice.configs;

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
    public static final String MONITOR_DEVICE_SYNC_QUEUE = "monitor-device-sync-queue";
    public static final String MONITOR_DEVICE_DELETE_QUEUE = "monitor-device-delete-queue";
    public static final String MONITOR_DEVICE_UPDATE_QUEUE = "monitor-device-update-queue";
    public static final String MONITOR_SIMULATOR_QUEUE = "monitor-simulator-queue";
    public static final String DEVICE_CREATED_ROUTING_KEY = "sync.device.created";
    public static final String DEVICE_DELETED_ROUTING_KEY = "sync.device.deleted";
    public static final String DEVICE_UPDATED_ROUTING_KEY = "sync.device.updated";
    public static final String SIMULATOR_ROUTING_KEY = "sync.monitor.generated";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Queue monitorDeviceSyncQueue() {
        return new Queue(MONITOR_DEVICE_SYNC_QUEUE, true);
    }

    @Bean
    public Binding monitorDeviceSyncBinding(Queue monitorDeviceSyncQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(monitorDeviceSyncQueue)
                .to(syncExchange)
                .with(DEVICE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue monitorDeviceDeleteQueue() {
        return new Queue(MONITOR_DEVICE_DELETE_QUEUE, true);
    }

    @Bean
    public Binding monitorDeviceDeleteBinding(Queue monitorDeviceDeleteQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(monitorDeviceDeleteQueue)
                .to(syncExchange)
                .with(DEVICE_DELETED_ROUTING_KEY);
    }

    @Bean
    public Queue monitorDeviceUpdateQueue() {
        return new Queue(MONITOR_DEVICE_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding monitorDeviceUpdateBinding(Queue monitorDeviceUpdateQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(monitorDeviceUpdateQueue)
                .to(syncExchange)
                .with(DEVICE_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Queue monitorSimulatorQueue() {
        return new Queue(MONITOR_SIMULATOR_QUEUE, true);
    }

    @Bean
    public Binding monitorSimulatorBinding(Queue monitorSimulatorQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(monitorSimulatorQueue)
                .to(syncExchange)
                .with(SIMULATOR_ROUTING_KEY);
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
