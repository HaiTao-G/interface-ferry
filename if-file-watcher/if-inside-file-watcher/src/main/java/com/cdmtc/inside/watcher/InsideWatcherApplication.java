package com.cdmtc.inside.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @ClassName InsideWatcherApplication
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 12:19
 */
@EnableRetry
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class InsideWatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsideWatcherApplication.class, args);
    }
}
