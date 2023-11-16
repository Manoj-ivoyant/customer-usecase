package com.ivoyant.customerusecase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivoyant.customerusecase.config.MQConfig;
import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.model.AddressIndex;
import com.ivoyant.customerusecase.model.CustomerIndex;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.index.IndexRequest;
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
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void consumeMessages(Customer customer) throws IOException {
        AddressIndex addressIndex = AddressIndex.builder().addressLane1(customer.getAddress().getAddressLane1())
                .addressLane2(customer.getAddress().getAddressLane2()).id(customer.getAddress().getId())
                .zip(customer.getAddress().getZip()).city(customer.getAddress().getCity()).
                state(customer.getAddress().getState()).build();
        CustomerIndex customerIndex = CustomerIndex.builder().conversationId(customer.getConversationId())
                .firstName(customer.getFirstName()).lastName(customer.getLastName()).email(customer.getEmail())
                .phone(customer.getPhone()).address(addressIndex).build();
        // Convert CustomerIndex object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customerIndex);

        // Create an index request with the JSON data
        IndexRequest indexRequest = new IndexRequest("customer")
                .source(customerJson, XContentType.JSON);

        // Execute the index request
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        // Handle the index response if needed
        String indexId = indexResponse.getId();
        LOGGER.info("Data indexed with ID: " + indexId);
    }

}

