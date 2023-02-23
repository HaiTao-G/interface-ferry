package com.cdmtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//开启缓存功能
@EnableCaching
public class DataSourceEhcacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceEhcacheApplication.class, args);
    }
}