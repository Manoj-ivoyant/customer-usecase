package com.ivoyant.customerusecase.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;

@Configuration
public class ElasticConfig {
    @Value("${spring.elasticsearch.uris}")
    private  String uris;
    @Value("${spring.elasticsearch.username}")
    private  String userName;
    @Value("${spring.elasticsearch.password}")
    private  String password;


    @Bean
    public RestHighLevelClient getClient() {
            final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(uris.split(","))
                    .withBasicAuth(userName, password)
                    .build();
            return RestClients.create(clientConfiguration).rest();
        }
    }



