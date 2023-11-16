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
import java.util.UUID;
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public String createCustomer(CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findByEmail(customerDto.getEmail());

        if (existingCustomer != null) {
            throw new CustomerConflictExists();
        }

        Address address = new Address();
        BeanUtils.copyProperties(customerDto.getAddressDto(), address);
        address.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        address.setState(StateConverter.convertToAbbreviation(address.getState()));
        addressRepository.save(address);

        Customer newCustomer = new Customer();
        BeanUtils.copyProperties(customerDto, newCustomer);
        newCustomer.setConversationId(UUID.randomUUID().toString());
        newCustomer.setAddress(address);
        customerRepository.save(newCustomer);

        LOGGER.info("Customer saved successfully");
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.MESSAGE_ROUTING_KEY, newCustomer);
        LOGGER.info("Message added to Queue by Message Broker RabbitMQ");

        return newCustomer.getConversationId();
    }
}
