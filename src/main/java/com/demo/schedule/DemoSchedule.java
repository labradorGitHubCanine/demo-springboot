package com.demo.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Async
@Component
public class DemoSchedule {

    @Scheduled(fixedDelay = 1000 * 5)
    public void schedule01() {
//        System.out.println(new Timestamp(System.currentTimeMillis()) + "\t每5秒执行一次");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void schedule02() {
        System.out.println(new Timestamp(System.currentTimeMillis()) + "\t每天1点执行一次");
    }

}
