package com.ivoyant.customerusecase.service.impl;

import com.ivoyant.customerusecase.config.MQConfig;
import com.ivoyant.customerusecase.dto.AddressDto;
import com.ivoyant.customerusecase.dto.CustomerDto;
import com.ivoyant.customerusecase.entity.Address;
import com.ivoyant.customerusecase.entity.Customer;
import com.ivoyant.customerusecase.exception.CustomerConflictExists;
import com.ivoyant.customerusecase.exception.CustomerNotFound;
import com.ivoyant.customerusecase.repository.AddressRepository;
import com.ivoyant.customerusecase.repository.CustomerRepository;
import com.ivoyant.customerusecase.service.CustomerService;
import com.ivoyant.customerusecase.utils.QueryResolver;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private QueryResolver queryResolver;

    @Override
    public String createCustomer(CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findByEmail(customerDto.getEmail());

        if (existingCustomer != null) {
            throw new CustomerConflictExists();
        }

        Address address = new Address();
        BeanUtils.copyProperties(customerDto.getAddressDto(), address);
        Long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        address.setId(id);
        address.setZip(Integer.parseInt(customerDto.getAddressDto().getZip()));
        address.setState(StateConverter.convertToAbbreviation(address.getState()));
        addressRepository.save(address);

        Customer newCustomer = new Customer();
        BeanUtils.copyProperties(customerDto, newCustomer);
        newCustomer.setConversationId(UUID.randomUUID().toString());
        newCustomer.setAddressId(id);
        customerRepository.save(newCustomer);
        LOGGER.info("Customer saved successfully");

        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE,MQConfig.CUSTOMER_ROUTING_KEY,newCustomer);
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE,MQConfig.ADDRESS_ROUTING_KEY,address);
        LOGGER.info("message published to Queue by MessageBroker");
        return newCustomer.getConversationId();
    }

    @Override
    public CustomerDto getByKey(String key) {
        Customer customer = queryResolver.findByConversationIdOrPhone(key,key);
        Address address = addressRepository.findById(customer.getAddressId()).orElseThrow(CustomerNotFound::new);
        AddressDto addressDto = AddressDto.builder().addressLane1(address.getAddressLane1())
                .addressLane2(address.getAddressLane2())
                .zip(address.getZip().toString()).state(address.getState()).city(address.getCity()).build();
        LOGGER.info("Customer found successfully");
        return CustomerDto.builder().firstName(customer.getFirstName()).lastName(customer.getLastName())
                .phone(customer.getPhone()).email(customer.getEmail()).addressDto(addressDto).build();


    }


    @Override
    public void deleteByKey(String key) {
        Customer customer = queryResolver.findByConversationIdOrPhone(key,key);
        Address address = addressRepository.findById(customer.getAddressId()).orElseThrow(CustomerNotFound::new);
        LOGGER.info("Customer with id deleted {}", key);
        customerRepository.delete(customer);
        addressRepository.delete(address);
        LOGGER.info("Customer deleted");
    }

    @Override
    public  String updateByKey(String key,CustomerDto customerDto){
        Customer customer= queryResolver.findByConversationIdOrPhone(key,key);
        Address address=addressRepository.findById(customer.getAddressId()).orElseThrow(CustomerNotFound::new);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        address.setAddressLane1(customerDto.getAddressDto().getAddressLane1());
        address.setAddressLane2(customerDto.getAddressDto().getAddressLane2());
        address.setCity(customerDto.getAddressDto().getCity());
        address.setZip(Integer.parseInt(customerDto.getAddressDto().getZip()));
        address.setState(StateConverter.convertToAbbreviation(customerDto.getAddressDto().getState()));
        addressRepository.save(address);
        customerRepository.save(customer);
        LOGGER.info("Customer updated");
        return "";
    }
}
