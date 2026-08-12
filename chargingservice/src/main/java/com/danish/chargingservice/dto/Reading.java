package com.danish.chargingservice.dto;

import com.danish.chargingservice.enums.MeterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Reading {
    private Long meterNumber;
    private Long readingValue;
    private Long readingId;
    private MeterType meterType;
}
