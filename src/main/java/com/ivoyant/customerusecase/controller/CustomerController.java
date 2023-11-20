package com.ivoyant.customerusecase.controller;

import com.ivoyant.customerusecase.dto.CustomerDto;
import com.ivoyant.customerusecase.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.createCustomer(customerDto));
    }

    @GetMapping("/{key}")
    public ResponseEntity<CustomerDto> getByKey(@Valid @PathVariable @NotBlank(message = "key is required field")
                                                String key) {
        return ResponseEntity.ok(customerService.getByKey(key));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> deleteByKey(@Valid @PathVariable @NotBlank(message = "key is required field")
                                                String key ){
        return ResponseEntity.ok(customerService.deleteByKey(key));
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> updateByKey(@Valid @PathVariable @NotBlank(message = "key is required field")
                                                  String key,@RequestBody CustomerDto customerDto){
        return ResponseEntity.ok(customerService.updateByKey(key,customerDto));
    }

}
