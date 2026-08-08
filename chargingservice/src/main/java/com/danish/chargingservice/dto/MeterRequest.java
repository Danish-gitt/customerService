package com.danish.chargingservice.dto;

import com.danish.chargingservice.enums.MeterStatus;
import com.danish.chargingservice.enums.MeterType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeterRequest {
    @NotNull
    @Min(10000)
    @Max(99999)
    private Long meterNumber;


    @NotNull
    @Min(1000000000L)
    @Max(9999999999L)
    private Long msisdn;

    @NotBlank
    private String customerName;

    @NotBlank
    private String address;

    @NotNull
    private MeterType meterType;

    @NotNull
    private MeterStatus meterStatus;
}
