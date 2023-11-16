package com.ivoyant.customerusecase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivoyant.customerusecase.config.MQConfig;
import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.model.AddressIndex;
import com.ivoyant.customerusecase.model.CustomerIndex;
import jakarta.annotation.PreDestroy;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticController.class);

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ElasticController(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void consumeMessages(Customer customer) {
        try {
            AddressIndex addressIndex = AddressIndex.builder()
                    .addressLane1(customer.getAddress().getAddressLane1())
                    .addressLane2(customer.getAddress().getAddressLane2())
                    .id(customer.getAddress().getId())
                    .zip(customer.getAddress().getZip())
                    .city(customer.getAddress().getCity())
                    .state(customer.getAddress().getState())
                    .build();

            CustomerIndex customerIndex = CustomerIndex.builder()
                    .conversationId(customer.getConversationId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .address(addressIndex)
                    .build();

            String customerJson = objectMapper.writeValueAsString(customerIndex);

            IndexRequest indexRequest = new IndexRequest("customer")
                    .source(customerJson, XContentType.JSON);

            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            String indexId = indexResponse.getId();
            LOGGER.info("Data indexed with ID: {}", indexId);
        } catch (IOException e) {
            LOGGER.error("Error processing message: {}", e.getMessage());
        }
    }


    @PreDestroy
    public void closeClient() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error closing RestHighLevelClient: {}", e.getMessage());
        }
    }
}
