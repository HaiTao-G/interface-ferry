package com.cdmtc.ftp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("ftp")
public class FtpConfig {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /** 类型ftp|sftp 默认 ftp */
    private String type = "ftp";

    /** 地址 */
    private String host;

    /** 端口 */
    private Integer port = 21;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 密钥 默认:空 */
    private String privateKey;

    /** 编码 默认:UTF-8 */
    private String encoding = "UTF-8";

    /** 是否被动模式 默认:true */
    private Boolean passiveMode = true;

    /** 文件存储路径 默认:/ */
    private String basePath = "/";

    /** 最小空闲数 默认:0 */
    private Integer minIdle = 0;

    /** 最大空闲数 默认:4 */
    private Integer maxIdle = 4;

    /** 最大连接数 默认:8 */
    private Integer maxTotal = 8;

    /** 最大连接建立等待时间 默认:10000 */
    private Integer maxWaitMillis = 10000;

    /** 是否进行空闲检测 默认:true */
    private Boolean testWhileIdle = true;

    /** 空闲连接回收线程执行周期 默认:60000 */
    private Long timeBetweenEvictionRunsMillis = 60000L;

    /** 可被回收的最小空闲时间 默认:600000 */
    private Long minEvictableIdleTimeMillis = 600000L;

    /** 获取时检测 默认:true */
    private Boolean testOnBorrow = true;

    /** 归还时检测 默认:false */
    private Boolean testOnReturn = false;

}
