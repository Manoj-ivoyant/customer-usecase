package com.ivoyant.customerusecase.utils;

import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.exception.CustomerNotFound;
import com.ivoyant.customerusecase.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryResolver {

    @Autowired
    private CustomerRepository customerRepository;

    public  Customer findByConversationIdOrPhone(String conversationId, String phone) {
        Customer customerByConversationId = customerRepository.findByConversationId(conversationId);
        Customer customerByPhone = customerRepository.findByPhone(phone);
        if(customerByConversationId==null||customerByPhone==null){
            throw new CustomerNotFound();
        }
        if(customerByConversationId!=null){
            return customerByConversationId;
        }
        else return customerByPhone;
    }

}
