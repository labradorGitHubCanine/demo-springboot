package com.demo.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Async
@Component
public class DemoSchedule {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(fixedDelay = 1000 * 60)
    public void schedule01() {
        System.out.println(LocalDateTime.now().format(FORMATTER) + "\t每60秒执行一次");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void schedule02() {
        System.out.println(LocalDateTime.now().format(FORMATTER) + "\t每天1点执行一次");
    }

}
