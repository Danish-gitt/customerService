package com.danish.chargingservice.entitiy;

import com.danish.chargingservice.enums.MeterType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="rate")
public class Rate {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="meterType",nullable = false)
    private MeterType meterType;


    @Column(name="rate",nullable = false)
    private Double rateValue;

}
