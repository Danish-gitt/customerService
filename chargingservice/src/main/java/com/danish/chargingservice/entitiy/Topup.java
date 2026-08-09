package com.danish.chargingservice.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="topup")
public class Topup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false,unique = true)
    private Long id;

    @Column(name="amount",nullable = false)
    private int amount;

    @CreationTimestamp
    @Column(name="createdOn",nullable = false,updatable = false)
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name="meterNumber")
    private Customer customer;

}
