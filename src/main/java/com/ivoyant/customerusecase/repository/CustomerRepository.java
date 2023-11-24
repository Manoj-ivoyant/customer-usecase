package com.ivoyant.customerusecase.repository;

import com.ivoyant.customerusecase.entity.Customer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CassandraRepository<Customer, String> {
    Customer findByEmail(String email);

    Customer findByConversationId(String conversationId);

    Customer findByPhone(String phone);

}
