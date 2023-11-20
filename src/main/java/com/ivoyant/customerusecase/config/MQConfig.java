package com.ivoyant.customerusecase.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String CUSTOMER_QUEUE = "customer_queue";
    public static final String ADDRESS_QUEUE = "address_queue";
    public static final String EXCHANGE = "msg_exchange";
    public static final String CUSTOMER_ROUTING_KEY = "customer_routing_key";
    public static final String ADDRESS_ROUTING_KEY = "address_routing_key";

    @Bean
    public Queue customerQueue() {
        return new Queue(CUSTOMER_QUEUE);
    }

    @Bean
    public Queue addressQueue() {
        return new Queue(ADDRESS_QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingCustomerQueue(Queue customerQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(customerQueue).to(topicExchange).with(CUSTOMER_ROUTING_KEY);
    }

    @Bean
    public Binding bindingAddressQueue(Queue addressQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(addressQueue).to(topicExchange).with(ADDRESS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
