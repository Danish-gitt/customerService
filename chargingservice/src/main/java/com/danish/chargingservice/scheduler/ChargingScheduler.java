package com.danish.chargingservice.scheduler;

import com.danish.chargingservice.service.Deduction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargingScheduler {
    private final Deduction deduction;

    @Scheduled(fixedRate = 30000)
    public void callDeduction(){
        log.info("Deduction Scheduler started");
        deduction.updateBalance();
        log.info("Deduction Scheduler completed");
    }


}
