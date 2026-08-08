package com.danish.chargingservice.service;

import com.danish.chargingservice.dto.CustomerRequest;
import com.danish.chargingservice.dto.MeterRequest;
import com.danish.chargingservice.dto.MeterResponse;
import com.danish.chargingservice.entitiy.Customer;
import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerManagement {
    private final CustomerRepo customerRepo;
    private final RestClient restClient;
    private final Random random = new Random();

    public Long generateMeterNumber(){
        while(true){
            Long randomValue = random.nextLong(10000,100000);
            if(!customerRepo.existsById(randomValue)){
                return randomValue;
            }
        }
    }

    public Customer addCustomer(CustomerRequest customerRequest){
        Customer newCustomer= new Customer();
        MeterRequest meterRequest = new MeterRequest();
        newCustomer.setMsisdn(customerRequest.getMsisdn());
        newCustomer.setCustomerName(customerRequest.getCustomerName());
        newCustomer.setAddress(customerRequest.getAddress());
        newCustomer.setMeterType(customerRequest.getMeterType());
        newCustomer.setBalance(BigDecimal.ZERO);
        Long generatedMeterNumber = generateMeterNumber();
        newCustomer.setMeterNumber(generatedMeterNumber);

        customerRepo.save(newCustomer);
        log.info("New customer added successfully :{}",newCustomer);

        meterRequest.setMeterNumber(generatedMeterNumber);
        meterRequest.setMsisdn(customerRequest.getMsisdn());
        meterRequest.setCustomerName(customerRequest.getCustomerName());
        meterRequest.setAddress(customerRequest.getAddress());
        meterRequest.setMeterType(customerRequest.getMeterType());
        meterRequest.setMeterStatus(MeterStatus.INACTIVE);



        ResponseEntity<MeterResponse> response = restClient.post()
                .uri("http://localhost:8080/meter/add")
                .body(meterRequest)
                .retrieve()
                .toEntity(MeterResponse.class);

        log.info("Meter API response: {}", response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Meter created successfully :{}",meterRequest);
        }

        return newCustomer;

    }
}
