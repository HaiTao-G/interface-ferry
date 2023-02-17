package com.cdmtc.fastdfs.config;

import com.cdmtc.fastdfs.FastDfsConstants;
import com.cdmtc.fastdfs.FastDfsProperties;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.FastDfsClientBuilder;
import com.ykrenz.fastdfs.config.FastDfsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @ClassName FastDfsAutoConfiguration
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/28 17:51
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(FastDfs.class)
@ConditionalOnProperty(name = FastDfsConstants.ENABLED, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FastDfsProperties.class)
@Slf4j
public class FastDfsAutoConfiguration {


    @ConditionalOnMissingBean
    @Bean
    public FastDfs fastDfs(FastDfsProperties properties) {
        Assert.isTrue(!CollectionUtils.isEmpty(properties.getTrackerServers()), "FastDFS trackerServers can't be empty.");
        FastDfsConfiguration configuration = new FastDfsConfiguration();
        configuration.setDefaultGroup(properties.getDefaultGroup());
        configuration.setHttp(properties.getHttp());
        configuration.setConnection(properties.getConnection());
        FastDfs fastDfs = new FastDfsClientBuilder().build(properties.getTrackerServers(), configuration);
        log.info("fastDfs Client init...{}", properties.getTrackerServers());
        return fastDfs;
    }
}
