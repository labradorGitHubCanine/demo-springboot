package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@MapperScan({"com.demo.mapper", "com.qmw.mapper"}) // mapper扫描
@EnableScheduling // 开启定时任务
@EnableAsync // 开启定时任务异步
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
