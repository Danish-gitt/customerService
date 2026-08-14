package com.danish.chargingservice.entitiy;

import com.danish.chargingservice.enums.MeterType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="deductionRecords")
public class DeductionRecords {
    @Id
    @Column(name="id",nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="meterNumber",nullable = false)
    private Long meterNumber;

    @Column(name="delta")
    private Long delta;

    @Column(name="curReadingId")
    private Long curReadingId;

    @Column(name="meterType")
    private MeterType meterType;

    @Column(name="prevReadingId")
    private Long prevReadingId;

    @Column(name="chargedAmount")
    private BigDecimal chargedAmount;

    @CreationTimestamp
    @Column(name="createdOn",nullable = false,updatable = false)
    private LocalDateTime createdOn;

}
