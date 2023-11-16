package com.ivoyant.customerusecase.service.impl;

import com.ivoyant.customerusecase.config.MQConfig;
import com.ivoyant.customerusecase.dto.CustomerDto;
import com.ivoyant.customerusecase.entity.Address;
import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.exception.CustomerConflictExists;
import com.ivoyant.customerusecase.repository.AddressRepository;
import com.ivoyant.customerusecase.repository.CustomerRepository;
import com.ivoyant.customerusecase.service.CustomerService;
import com.ivoyant.customerusecase.utils.StateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RabbitTemplate template;

    private static final Logger LOGGER= LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Override
    public String createCustomer(CustomerDto customerDto) {
        Customer customer=customerRepository.findByEmail(customerDto.getEmail());
        if(customer==null){
            Customer customer1=new Customer();
            Address address=new Address();
            BeanUtils.copyProperties(customerDto,customer1);
            customer1.setConversationId(UUID.randomUUID().toString());
            BeanUtils.copyProperties(customerDto.getAddressDto(), address);
            Long id=new Random().nextLong();
            address.setId(id);
            String state=address.getState();
            String keyState=StateConverter.convertToAbbreviation(state);
            address.setState(keyState);
            addressRepository.save(address);
            customer1.setAddress(address);
            customerRepository.save(customer1);
            LOGGER.info("customer saved successfully");
            template.convertAndSend(MQConfig.EXCHANGE,MQConfig.MESSAGE_ROUTING_KEY,customer1);
            LOGGER.info("Message added to Queue by MessageBroker RabbitMQ");
            return customer1.getConversationId();
        }
        else{
            throw new CustomerConflictExists();
        }
    }
}
