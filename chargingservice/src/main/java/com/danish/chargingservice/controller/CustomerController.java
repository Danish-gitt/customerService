package com.danish.chargingservice.controller;

import com.danish.chargingservice.dto.CustomerRequest;
import com.danish.chargingservice.dto.CustomerResponse;
import com.danish.chargingservice.entitiy.Customer;
import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.service.CustomerManagement;
import com.danish.chargingservice.service.TopupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerManagement customerManagement;
    private final TopupService topupService;

    @PostMapping("/add")
    public ResponseEntity<CustomerResponse> addCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        Customer savedCustomer = customerManagement.addCustomer(customerRequest);
        CustomerResponse response = new CustomerResponse();
        response.setMeterNumber(savedCustomer.getMeterNumber());
        response.setMsisdn(savedCustomer.getMsisdn());
        response.setCustomerName(savedCustomer.getCustomerName());
        response.setAddress(savedCustomer.getAddress());
        response.setMeterType(savedCustomer.getMeterType());
        response.setMeterStatus(MeterStatus.INACTIVE);
        response.setBalance(savedCustomer.getBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/topup/{meterNumber}/{amount}")
    public ResponseEntity<CustomerResponse> topup(@PathVariable Long meterNumber,@PathVariable int amount){
        return ResponseEntity.status(HttpStatus.CREATED).body(topupService.addBalance(meterNumber,amount));
    }


}
