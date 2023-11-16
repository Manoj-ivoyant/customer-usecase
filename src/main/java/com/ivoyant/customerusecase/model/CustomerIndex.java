package com.ivoyant.customerusecase.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Builder
@Document(indexName = "customer")
public class CustomerIndex implements Serializable {
    @Id
    private String conversationId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private AddressIndex address;
}
