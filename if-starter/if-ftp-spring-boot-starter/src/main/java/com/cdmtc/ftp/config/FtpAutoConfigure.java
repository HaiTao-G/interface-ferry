package com.cdmtc.ftp.config;

import cn.hutool.extra.ftp.AbstractFtp;
import com.cdmtc.ftp.factory.FtpClientFactory;
import com.cdmtc.ftp.pool.FtpClientPool;
import com.cdmtc.ftp.service.FtpService;
import com.cdmtc.ftp.service.FtpUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@ConditionalOnClass(FtpService.class)
@EnableConfigurationProperties(FtpConfig.class)
@ConditionalOnProperty(prefix = "ftp", name = "enabled",havingValue = "true")
public class FtpAutoConfigure {

    @PostConstruct
    public void initDir() {
        File ftleTempPath = new File(FtpUtils.FILE_TEMP_PATH);
        if (!ftleTempPath.exists()) {
            ftleTempPath.mkdirs();
        }
    }

    @Bean
    public FtpClientFactory getFtpClientFactory(FtpConfig ftpProperties) {
        return new FtpClientFactory(ftpProperties);
    }

    @Bean
    public FtpClientPool getFtpClientPool(FtpConfig ftpConfig, FtpClientFactory ftpClientFactory) {
        GenericObjectPoolConfig<AbstractFtp> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(ftpConfig.getMinIdle());
        poolConfig.setMaxIdle(ftpConfig.getMaxIdle());
        poolConfig.setMaxTotal(ftpConfig.getMaxTotal());
        poolConfig.setTestWhileIdle(ftpConfig.getTestWhileIdle());
        poolConfig.setTimeBetweenEvictionRunsMillis(ftpConfig.getTimeBetweenEvictionRunsMillis());
        poolConfig.setMinEvictableIdleTimeMillis(ftpConfig.getMinEvictableIdleTimeMillis());
        poolConfig.setTestOnBorrow(ftpConfig.getTestOnBorrow());
        poolConfig.setTestOnReturn(ftpConfig.getTestOnReturn());
        poolConfig.setJmxEnabled(false);
        return new FtpClientPool(ftpClientFactory, poolConfig);
    }

    @Bean
    public FtpService getFtpService(FtpClientPool ftpClientPool) {
        return new FtpService(ftpClientPool);
    }
}