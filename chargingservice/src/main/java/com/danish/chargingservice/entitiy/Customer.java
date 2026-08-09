package com.danish.chargingservice.entitiy;

import com.danish.chargingservice.enums.MeterType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customer")
@Data
public class Customer {

    @Id
    @Column(name="meterNumber",nullable = false,unique = true)
    private Long meterNumber;

    @Column(name="msisdn",nullable = false)
    private Long msisdn;

    @Column(name="customerName",nullable = false)
    private String customerName;

    @Column(name="address",nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name="meterType",nullable = false)
    private MeterType meterType;

    @Column(name="balance",nullable = false)
    private BigDecimal balance;

    @CreationTimestamp
    @Column(name="createdOn",nullable = false,updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name="updatedOn",nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Topup> topups = new ArrayList<>();


}
