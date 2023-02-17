package com.cdmtc.screenshot.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yxl
 * @date 2022/8/26
 * @description chrome远程截图配置
 */
@Data
@ConfigurationProperties(prefix = "chromedriver")
public class ChromeDriverRemoteConfig {

    public Boolean enable;

    /**
     * 代理ip
     */
    public String ip;

    /**
     * 代理端口
     */
    public Integer port;

    /**
     * Selenium Grid地址
     */
    public String remoteUrl;

    /**
     * 浏览器页面过期时间
     */
    public Integer pageLoadTimeout = 60;

    /**
     * 浏览器页面加载时间
     */
    public Integer webWaitTimeout = 30;
}
