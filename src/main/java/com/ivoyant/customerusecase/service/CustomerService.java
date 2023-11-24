package com.ivoyant.customerusecase.service;

import com.ivoyant.customerusecase.dto.CustomerDto;


public interface CustomerService {
    String createCustomer(CustomerDto customerDto);

     void deleteByKey(String key);
    CustomerDto getByKey(String key);

    String updateByKey(String key,CustomerDto customerDto);
}
