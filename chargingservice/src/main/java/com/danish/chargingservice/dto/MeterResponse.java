package com.danish.chargingservice.dto;

import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.enums.MeterType;
import lombok.Data;

@Data
public class MeterResponse {
    private Long meterNumber;
    private Long msisdn;
    private String customerName;
    private String address;
    private MeterType meterType;
    private MeterStatus meterStatus;
}
