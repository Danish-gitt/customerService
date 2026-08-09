package com.danish.chargingservice.service;

import com.danish.chargingservice.dto.CustomerResponse;
import com.danish.chargingservice.dto.MeterResponse;
import com.danish.chargingservice.entitiy.Customer;
import com.danish.chargingservice.entitiy.Topup;
import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.exception.MeterNotFoundException;
import com.danish.chargingservice.repository.CustomerRepo;
import com.danish.chargingservice.repository.TopupRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopupService {
    private final TopupRepo topupRepo;
    private final CustomerRepo customerRepo;
    private final RestClient restClient;

    public CustomerResponse addBalance(Long meterNumber,int amount){
        if(customerRepo.existsById(meterNumber)){
            Customer foundCustomer = customerRepo.findById(meterNumber).get();
            BigDecimal currentBalance = foundCustomer.getBalance();
            BigDecimal newBalance = currentBalance.add(BigDecimal.valueOf(amount));
            foundCustomer.setBalance(newBalance);
            customerRepo.save(foundCustomer);
            log.info("Topup done successfully for customer: {}",meterNumber);

            Topup topup = new Topup();
            topup.setAmount(amount);
            topup.setCustomer(foundCustomer);
            topupRepo.save(topup);
            log.info("Entry made in topup table: {}",topup);

            CustomerResponse response = new CustomerResponse();
            response.setMeterNumber(foundCustomer.getMeterNumber());
            response.setMsisdn(foundCustomer.getMsisdn());
            response.setCustomerName(foundCustomer.getCustomerName());
            response.setAddress(foundCustomer.getAddress());
            response.setMeterType(foundCustomer.getMeterType());

            ResponseEntity<MeterStatus> statusResponse = restClient.get()
                    .uri("http://localhost:8080/meter/getStatus/{meterNumber}",meterNumber)
                    .retrieve()
                    .toEntity(MeterStatus.class);

            response.setMeterStatus(statusResponse.getBody());
            response.setBalance(foundCustomer.getBalance());

            return response;

        }else{
            log.warn("Meter Number does not exists: {}",meterNumber);
            throw new MeterNotFoundException("Meter Number does not exists: "+meterNumber);
        }

    }
}
