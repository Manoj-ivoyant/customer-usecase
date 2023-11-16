package com.ivoyant.customerusecase.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType("address") // Annotation for User-Defined Type
public class Address implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED) // Define id as a primary key
    private Long id;
    private String addressLane1;
    private String addressLane2;
    private String city;
    private Integer zip;
    private String state;
}
