package com.cdmtc.fastdfs.endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName FastDfsEndpointAutoConfiguration
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/28 17:58
 */
@ConditionalOnClass(IntegrationProperties.Endpoint.class)
public class FastDfsEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FastDfsEndpoint fastDfsEndpoint() {
        return new FastDfsEndpoint();
    }

}
