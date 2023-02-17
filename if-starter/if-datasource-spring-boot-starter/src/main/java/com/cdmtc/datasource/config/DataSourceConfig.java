package com.cdmtc.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName DataSourceConfig
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/26 20:56
 */
@Data
@ConfigurationProperties("datasource")
public class DataSourceConfig {

    /**
     * 是否启用datasource
     */
    private boolean enabled = true;
    /**
     * mysql连接url
     */
    private String url;
    /**
     * 驱动连接
     */
    private String driverClassName;
    /**
     * 账号
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
}
