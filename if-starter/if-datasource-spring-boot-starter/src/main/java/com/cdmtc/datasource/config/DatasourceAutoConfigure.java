package com.cdmtc.datasource.config;

import com.cdmtc.datasource.dao.InstructionDao;
import com.cdmtc.datasource.dao.impl.InstructionDaoImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName JdbcTemplateConfig
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/26 21:04
 */
@Configuration
@ConditionalOnClass(InstructionDao.class)
@EnableConfigurationProperties(DataSourceConfig.class)
public class DatasourceAutoConfigure {

    @Autowired
    private DataSourceConfig properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "datasource", value = "enabled", havingValue = "true")
    public InstructionDao instructionDao(@Qualifier("myJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new InstructionDaoImpl(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "datasource", value = "enabled", havingValue = "true")
    public JdbcTemplate myJdbcTemplate() {
        // 创建基础hikari数据源
        DataSourceBuilder<HikariDataSource> hikariDataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);
        HikariDataSource hikariDataSource = hikariDataSourceBuilder
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUserName())
                .password(properties.getPassword())
                .build();
        /**
         * 配置Hikari连接池
         */
        //update自动提交设置
        hikariDataSource.setAutoCommit(true);
        //连接查询语句设置
        hikariDataSource.setConnectionTestQuery("select 1");
        //连接超时时间设置
        hikariDataSource.setConnectionTimeout(3000);
        //连接空闲生命周期设置
        hikariDataSource.setIdleTimeout(3000);
        //执行查询启动设置
        hikariDataSource.setIsolateInternalQueries(false);
        //连接池允许的最大连接数量
        hikariDataSource.setMaximumPoolSize(3000);
        //检查空余连接优化连接池设置时间,单位毫秒
        hikariDataSource.setMaxLifetime(1800000);
        //连接池保持最小空余连接数量
        hikariDataSource.setMinimumIdle(10);
        //连接池名称
        hikariDataSource.setPoolName("hikariPool");
        return new JdbcTemplate(hikariDataSource);
    }
}

