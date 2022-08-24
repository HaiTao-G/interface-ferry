package com.cdmtc.ftp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("ftp")
public class FtpConfig {
    private boolean enabled = true;
    private String host;
    private int port = 21;
    private String userName;
    private String password;
    private boolean passiveMode = false;
    private String encoding = "UTF-8";
    private int connectTimeout = 30000;
    private int bufferSize = 8096;
    private int transferFileType;
}
