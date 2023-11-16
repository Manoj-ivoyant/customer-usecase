package com.ivoyant.customerusecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "address")
public class AddressIndex implements Serializable {
    @Id
    private Long id;
    private String addressLane1;
    private String addressLane2;
    private String city;
    private Integer  zip;
    private String state;
}
