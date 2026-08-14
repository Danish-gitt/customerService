package com.danish.chargingservice.repository;

import com.danish.chargingservice.entitiy.DeductionRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeductionRecordsRepo extends JpaRepository<DeductionRecords,Long> {
    Optional<DeductionRecords> findTopByMeterNumberOrderByIdDesc(Long meterNumber);
}
