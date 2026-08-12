package com.danish.chargingservice.repository;

import com.danish.chargingservice.entitiy.Rate;
import com.danish.chargingservice.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepo extends JpaRepository<Rate, MeterType> {
}
