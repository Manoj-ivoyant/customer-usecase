package com.ivoyant.customerusecase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivoyant.customerusecase.config.MQConfig;
import com.ivoyant.customerusecase.entity.Address;
import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.exception.CustomerNotFound;
import com.ivoyant.customerusecase.model.AddressIndex;
import com.ivoyant.customerusecase.model.CustomerIndex;
import com.ivoyant.customerusecase.repository.AddressRepository;
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
    private AddressRepository addressRepository;

    @Autowired
    public ElasticController(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = MQConfig.ADDRESS_QUEUE)
    public void consumeAddressMessage(Address address) {
        try {
            AddressIndex addressIndex = AddressIndex.builder()
                    .addressLane1(address.getAddressLane1())
                    .addressLane2(address.getAddressLane2())
                    .id(address.getId())
                    .zip(address.getZip())
                    .city(address.getCity())
                    .state(address.getState())
                    .build();

            String addressJson = objectMapper.writeValueAsString(addressIndex);
            IndexRequest indexRequest = new IndexRequest("address").source(addressJson, XContentType.JSON);

            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            String indexId = indexResponse.getId();
            LOGGER.info("Address data indexed with ID: {}", indexId);
        } catch (IOException e) {
            LOGGER.error("Error processing address message: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = MQConfig.CUSTOMER_QUEUE)
    public void consumeCustomerMessage(Customer customer) {
        try {
            AddressIndex addressIndex = getAddressIndexFromCustomer(customer); // Get AddressIndex from Customer

            CustomerIndex customerIndex = CustomerIndex.builder()
                    .conversationId(customer.getConversationId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .address(addressIndex)
                    .build();

            String customerJson = objectMapper.writeValueAsString(customerIndex);
            IndexRequest indexRequest = new IndexRequest("customer").source(customerJson, XContentType.JSON);

            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            String indexId = indexResponse.getId();
            LOGGER.info("Customer data indexed with ID: {}", indexId);
        } catch (IOException e) {
            LOGGER.error("Error processing customer message: {}", e.getMessage());
        }
    }

    private AddressIndex getAddressIndexFromCustomer(Customer customer) {

        Address address = addressRepository.findById(customer.getAddressId()).orElseThrow(() -> new CustomerNotFound());
        return AddressIndex.builder()
                .addressLane1(address.getAddressLane1())
                .addressLane2(address.getAddressLane2())
                .id(address.getId())
                .zip(address.getZip())
                .city(address.getCity())
                .state(address.getState())
                .build();
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
