package com.ivoyant.customerusecase.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Address implements Serializable {

    @PrimaryKey
    private Long id;
    private String addressLane1;
    private String addressLane2;
    private String city;
    private Integer zip;
    private String state;
}
