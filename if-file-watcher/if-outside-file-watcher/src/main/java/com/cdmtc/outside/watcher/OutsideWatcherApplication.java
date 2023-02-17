package com.cdmtc.outside.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @ClassName OutsideWatcherApplication
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 9:48
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OutsideWatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutsideWatcherApplication.class, args);
    }
}
