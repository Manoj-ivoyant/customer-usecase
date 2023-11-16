package com.ivoyant.customerusecase.service;

import com.ivoyant.customerusecase.dto.CustomerDto;
import org.springframework.stereotype.Service;


public interface CustomerService {
    public String createCustomer(CustomerDto customerDto);
}
