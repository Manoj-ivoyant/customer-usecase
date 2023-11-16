package com.ivoyant.customerusecase.repository;

import com.ivoyant.customerusecase.entity.Address;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CassandraRepository<Address,Long> {
}
