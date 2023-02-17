package com.cdmtc.curator.config;

import com.cdmtc.curator.client.CuratorClient;
import com.cdmtc.curator.exception.CuratorClientException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.utils.DefaultZookeeperFactory;
import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ThreadFactory;

@Configuration
@ConditionalOnClass(CuratorClient.class)
@EnableConfigurationProperties(CuratorClientConfig.class)
@ConditionalOnProperty(prefix = "curator-client", value = "enabled", havingValue = "true")
public class CuratorClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EnsembleProvider ensembleProvider(CuratorClientConfig curatorClientConfig) {
        return new FixedEnsembleProvider(curatorClientConfig.getConnectionString());
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryPolicy retryPolicy(CuratorClientConfig curatorClientConfig) {
        CuratorClientConfig.Retry retry = curatorClientConfig.getRetry();
        return new BoundedExponentialBackoffRetry(retry.getBaseSleepTimeMs(), retry.getMaxSleepTimeMs(), retry.getMaxRetries());
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressionProvider compressionProvider() {
        return new GzipCompressionProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public ZookeeperFactory zookeeperFactory() {
        return new DefaultZookeeperFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ACLProvider aclProvider() {
        return new ACLProvider() {
            @Override
            public List<ACL> getDefaultAcl() {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public CuratorFrameworkFactory.Builder builder(EnsembleProvider ensembleProvider,
                                                   RetryPolicy retryPolicy,
                                                   CompressionProvider compressionProvider,
                                                   ZookeeperFactory zookeeperFactory,
                                                   ACLProvider aclProvider,
                                                   CuratorClientConfig curatorClientConfig) {
        String charset = curatorClientConfig.getCharset();
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .ensembleProvider(ensembleProvider)
                .retryPolicy(retryPolicy)
                .compressionProvider(compressionProvider)
                .zookeeperFactory(zookeeperFactory)
                .namespace(curatorClientConfig.getNamespace())
                .sessionTimeoutMs(curatorClientConfig.getSessionTimeoutMs())
                .connectionTimeoutMs(curatorClientConfig.getConnectionTimeoutMs())
                .maxCloseWaitMs(curatorClientConfig.getMaxCloseWaitMs())
                .defaultData(curatorClientConfig.getDefaultData().getBytes(Charset.forName(charset)))
                .canBeReadOnly(curatorClientConfig.isCanBeReadOnly());
        if (!curatorClientConfig.isUseContainerParentsIfAvailable()) {
            builder.dontUseContainerParents();
        }
        CuratorClientConfig.Auth auth = curatorClientConfig.getAuth();
        if (StringUtils.isNotBlank(auth.getAuth())) {
            builder.authorization(auth.getScheme(), auth.getAuth().getBytes(Charset.forName(charset)));
            builder.aclProvider(aclProvider);
        }
        String threadFactoryClassName = curatorClientConfig.getThreadFactoryClassName();
        if (StringUtils.isNotBlank(threadFactoryClassName)) {
            try {
                Class cls = Class.forName(threadFactoryClassName);
                ThreadFactory threadFactory = (ThreadFactory) cls.newInstance();
                builder.threadFactory(threadFactory);
            } catch (ClassNotFoundException|IllegalAccessException|InstantiationException e) {
                throw new CuratorClientException("自动配置CuratorClient错误", e);
            }
        }
        return builder;
    }

    @Bean(initMethod = "init", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public CuratorClient curatorClient(CuratorFrameworkFactory.Builder builder) {
        return new CuratorClient(builder);
    }

}
