package com.danish.chargingservice.repository;

import com.danish.chargingservice.entitiy.Topup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopupRepo extends JpaRepository<Topup,Long> {
}
