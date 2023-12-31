package com.ivoyant.customerusecase.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Customer implements Serializable {
    @PrimaryKey
    private String conversationId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Address address;
}
