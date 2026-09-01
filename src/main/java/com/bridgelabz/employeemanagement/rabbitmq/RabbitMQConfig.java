package com.bridgelabz.employeemanagement.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "employee.exchange";

    public static final String QUEUE =
            "employee.upload.queue";

    public static final String ROUTING_KEY =
            "employee.upload";

    @Bean
    public DirectExchange employeeExchange() {

        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue employeeUploadQueue() {

        return new Queue(
                QUEUE,
                true
        );
    }

    @Bean
    public Binding employeeBinding(
            Queue employeeUploadQueue,
            DirectExchange employeeExchange) {

        return BindingBuilder
                .bind(employeeUploadQueue)
                .to(employeeExchange)
                .with(ROUTING_KEY);
    }
}