package com.ivoyant.customerusecase.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Customer journey application using spring boot")
                .summary("This application contains rest Api's which are used to register the customer, " +
                        "update the customer,and get the customer details.."));

    }
}
