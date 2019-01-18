package com.river.leader.config.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.river.leader.config.properties.DruidProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 多数据源配置
 */
@Configuration
@ConditionalOnProperty(name = {"spring.muti-datasource"}, matchIfMissing = false, havingValue = "false")
@EnableTransactionManagement
public class SingleDataSourceConfig {

    /**
     * druid配置
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.infra-db")
    public DruidProperties druidProperties() {
        return new DruidProperties();
    }

    /**
     * 单数据源连接池配置
     */
    @Bean
    public DruidDataSource dataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }

}

