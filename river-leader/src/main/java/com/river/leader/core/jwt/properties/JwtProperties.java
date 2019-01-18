package com.river.leader.core.jwt.properties;

import lombok.Data;

/**
 * jwt相关配置
 */
@Data
public class JwtProperties {

    /**
     * jwt的秘钥
     */
    private String secret = "smartcloud_C421AAEE0D114E9C";

    /**
     * jwt的发布者
     */
    private String issuer = "smartcloud";

    /**
     * jwt的接收者
     */
    private String audience = "everone";

    /**
     * jwt过期时间(单位:秒)(默认:1天)
     */
    private Long expiration = 82800L;


}
