package com.milotnt.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.milotnt.service.CommonSiteReservationService;

@Component
public class ReservationCleanUpTask {

    @Autowired
    private CommonSiteReservationService commonSiteReservationService;

    @Scheduled(cron = "0 58 17 * * ?")
    public void cleanExpiredCommonSiteReservations() {
        commonSiteReservationService.deleteExpiredUnCheckedReservations();
    }
}