package com.ivoyant.customerusecase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
public class CustomerUsecaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerUsecaseApplication.class, args);
    }

}
