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
        CustomerResponse response = new CustomerResponse();
        if(customerRepo.existsById(meterNumber)){
            //Adding balance
            Customer foundCustomer = customerRepo.findById(meterNumber).get();
            BigDecimal currentBalance = foundCustomer.getBalance();
            BigDecimal newBalance = currentBalance.add(BigDecimal.valueOf(amount));
            foundCustomer.setBalance(newBalance);
            customerRepo.save(foundCustomer);
            log.info("Topup done successfully for customer: {}",meterNumber);


            //Making topup entry
            Topup topup = new Topup();
            topup.setAmount(amount);
            topup.setCustomer(foundCustomer);
            topupRepo.save(topup);
            log.info("Entry made in topup table: {}",topup);

            //Checking current meterStatus
            ResponseEntity<MeterStatus> statusResponse = restClient.get()
                    .uri("http://localhost:8080/meter/getStatus/{meterNumber}",meterNumber)
                    .retrieve()
                    .toEntity(MeterStatus.class);



            //Changing meter status
            if(statusResponse.getBody()==MeterStatus.INACTIVE && foundCustomer.getBalance().compareTo(BigDecimal.TEN)>0){
                ResponseEntity<MeterResponse> newStatus=restClient.post()
                        .uri("http://localhost:8080/meter/update/{meterNumber}/{meterStatus}",meterNumber,MeterStatus.ACTIVE)
                        .retrieve()
                        .toEntity(MeterResponse.class);
                if (newStatus.getStatusCode().is2xxSuccessful()) {
                    log.info("MeterStatus updated to ACTIVE: {}", meterNumber);
                    response.setMeterStatus(MeterStatus.ACTIVE);
                } else {
                    log.warn("Failed to update meter status for: {}", meterNumber);
                    response.setMeterStatus(statusResponse.getBody());
                }
            }else{
                log.warn("Meter Status will remain same for the meter: {}{}",meterNumber,statusResponse.getBody());
                response.setMeterStatus(statusResponse.getBody());
            }

            //creating response
            response.setMeterNumber(foundCustomer.getMeterNumber());
            response.setMsisdn(foundCustomer.getMsisdn());
            response.setCustomerName(foundCustomer.getCustomerName());
            response.setAddress(foundCustomer.getAddress());
            response.setMeterType(foundCustomer.getMeterType());
            response.setBalance(foundCustomer.getBalance());
            return response;

        }else{
            log.warn("Meter Number does not exists: {}",meterNumber);
            throw new MeterNotFoundException("Meter Number does not exists: "+meterNumber);
        }

    }
}
