package com.danish.chargingservice.service;

import com.danish.chargingservice.dto.MeterResponse;
import com.danish.chargingservice.dto.Reading;
import com.danish.chargingservice.entitiy.Customer;
import com.danish.chargingservice.entitiy.DeductionRecords;
import com.danish.chargingservice.entitiy.Rate;
import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.enums.MeterType;
import com.danish.chargingservice.exception.ChargingAlreadyDoneException;
import com.danish.chargingservice.repository.CustomerRepo;
import com.danish.chargingservice.repository.DeductionRecordsRepo;
import com.danish.chargingservice.repository.RateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Deduction {
    private final CustomerRepo customerRepo;
    private final RestClient restClient;
    private final RateRepo rateRepo;
    private final DeductionRecordsRepo deductionRecordsRepo;

    public BigDecimal deductableBalance(Long meterNumber){
        BigDecimal chargedAmount;

        //fetching latest two readings from meterService
        ParameterizedTypeReference<List<Reading>> type = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Reading>> readingResponse = restClient.get()
                .uri("http://localhost:8080/meter/getReadings/{meterNumber}",meterNumber)
                .retrieve()
                .toEntity(type);


        //Checking if charging is already done
        Optional<DeductionRecords> deductionRecords = deductionRecordsRepo.findTopByMeterNumberOrderByIdDesc(meterNumber);
        if(deductionRecords.isPresent()){
        Long storedReadingId = deductionRecords.get().getCurReadingId();
        Long foundReadingId = readingResponse.getBody().get(0).getReadingId();

        if(storedReadingId.equals(foundReadingId)){
            log.warn("Charging already done for this reading: {}",deductionRecords.get().getId());
            return BigDecimal.ZERO;
            }
        }

        //Doing charging
        Long first = readingResponse.getBody().get(0).getReadingValue();
        Long second = readingResponse.getBody().get(1).getReadingValue();
        Long orgDelta = first-second;
        Long positiveDelta = Math.abs(orgDelta);
        MeterType meterType = readingResponse.getBody().get(0).getMeterType();
        Optional<Rate> rate = rateRepo.findById(meterType);
        Double rateValue = rate.get().getRateValue();
        chargedAmount = BigDecimal.valueOf(positiveDelta).multiply(BigDecimal.valueOf(rateValue));


        //Saving deduction records
        DeductionRecords newDeductionRecord = new DeductionRecords();
        newDeductionRecord.setMeterNumber(meterNumber);
        newDeductionRecord.setCurReadingId(readingResponse.getBody().get(0).getReadingId());
        newDeductionRecord.setPrevReadingId(readingResponse.getBody().get(1).getReadingId());
        newDeductionRecord.setMeterType(meterType);
        newDeductionRecord.setChargedAmount(chargedAmount);
        newDeductionRecord.setDelta(positiveDelta);
        deductionRecordsRepo.save(newDeductionRecord);

        return chargedAmount;
    }



    public BigDecimal getCurrentBalance(Long meterNumber){
        Optional<Customer> foundCustomer = customerRepo.findById(meterNumber);
        return foundCustomer.get().getBalance();
    }

   public void updateBalance(){
       ParameterizedTypeReference<List<MeterResponse>> type = new ParameterizedTypeReference<>() {};
       ResponseEntity<List<MeterResponse>> activeMetersList =restClient.get()
               .uri("http://localhost:8080/meter/getActiveMeters")
               .retrieve()
               .toEntity(type);


       for(MeterResponse singleMeter : activeMetersList.getBody()){
           Long meterNumber = singleMeter.getMeterNumber();
           BigDecimal currentBalance = getCurrentBalance(meterNumber);
           BigDecimal deductBalance = deductableBalance(meterNumber);
           BigDecimal newBalance = currentBalance.subtract(deductBalance);
           Optional<Customer> customer = customerRepo.findById(meterNumber);
           customer.get().setBalance(newBalance);
           customerRepo.save(customer.get());

           if(customer.get().getBalance().compareTo(BigDecimal.ZERO)<=0){
               ResponseEntity<MeterResponse> newStatus=restClient.post()
                       .uri("http://localhost:8080/meter/update/{meterNumber}/{meterStatus}",meterNumber,MeterStatus.INACTIVE)
                       .retrieve()
                       .toEntity(MeterResponse.class);
               if (newStatus.getStatusCode().is2xxSuccessful()) {
                   log.info("MeterStatus updated to INACTIVE: {}", meterNumber);
               } else {
                   log.warn("Failed to update meter status for: {}", meterNumber);
               }
           }
       }
   }




}
