package com.ivoyant.customerusecase.controller;

import com.ivoyant.customerusecase.dto.CustomerDto;
import com.ivoyant.customerusecase.service.CustomerService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
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

    @Timed(value = "customerController.execution.time", description = "Execution time of CustomerController")
    @Counted(value = "customerController.invocation.count", description = "Invocation count of CustomerController")
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
    public void deleteByKey(@Valid @PathVariable @NotBlank(message = "key is required field")
                                                String key ){
        customerService.deleteByKey(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> updateByKey(@Valid @PathVariable @NotBlank(message = "key is required field")
                                                  String key,@RequestBody CustomerDto customerDto){
        return ResponseEntity.ok(customerService.updateByKey(key,customerDto));
    }

}
