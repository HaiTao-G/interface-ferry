package com.cdmtc.screenshottooldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName ScreenShotToolDemoApplication
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 14:07
 */
@SpringBootApplication
@EnableAsync
public class ScreenShotToolDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScreenShotToolDemoApplication.class, args);
    }
}
