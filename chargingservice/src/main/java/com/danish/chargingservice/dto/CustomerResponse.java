package com.danish.chargingservice.dto;

import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.enums.MeterType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerResponse {
    private Long meterNumber;
    private Long msisdn;
    private String customerName;
    private String address;
    private MeterType meterType;
    private MeterStatus meterStatus;
    private BigDecimal balance;
}
