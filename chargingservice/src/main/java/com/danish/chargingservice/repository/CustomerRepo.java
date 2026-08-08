package com.danish.chargingservice.repository;

import com.danish.chargingservice.entitiy.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.hibernate.boot.model.NamedEntityGraphDefinition.Source.JPA;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
