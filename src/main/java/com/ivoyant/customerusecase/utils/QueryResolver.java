package com.ivoyant.customerusecase.utils;

import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.exception.CustomerNotFound;
import com.ivoyant.customerusecase.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryResolver {

    @Autowired
    private static  CustomerRepository customerRepository;

    public static Customer findByConversationIdOrPhone(String conversationId, String phone) {
        Customer customerByConversationId = customerRepository.findByConversationId(conversationId)
                .orElseThrow(()->new CustomerNotFound());
        Customer customerByPhone = customerRepository.findByPhone(phone).orElseThrow(()->new CustomerNotFound());
        if(customerByConversationId!=null){
            return customerByConversationId;
        }
        else return customerByPhone;
    }

}
