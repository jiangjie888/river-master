package com.river.leader.core.jwt.config;

import com.river.leader.core.jwt.properties.JwtProperties;
import com.river.leader.core.jwt.utils.JwtTokenUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * jwt自动配置
 */
@Configuration
public class JwtAutoConfiguration {

    /**
     * jwt token的配置
     */
    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    /**
     * jwt工具类
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(JwtProperties jwtProperties) {
        return new JwtTokenUtil(jwtProperties.getSecret(),jwtProperties.getIssuer(),jwtProperties.getAudience(),jwtProperties.getExpiration());
    }
}

