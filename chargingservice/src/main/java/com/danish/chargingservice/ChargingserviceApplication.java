package com.danish.chargingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChargingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargingserviceApplication.class, args);
	}

}
