package com.river.leader.core.jwt.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>jwt token工具类</p>
 * <pre>
 *     jwt的claim里一般包含以下几种数据:
 *         1. iss -- token的发行者
 *         2. sub -- 该JWT所面向的用户
 *         3. aud -- 接收该JWT的一方
 *         4. exp -- token的失效时间
 *         5. nbf -- 在此时间段之前,不会被处理
 *         6. iat -- jwt发布时间
 *         7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 */
public class JwtTokenUtil {

    //private JwtProperties jwtProperties;

    /**
     * jwt的秘钥
     */
    private static String jwtSecret;

    private static String jwtIssuer;

    private static String jwtAudience;

    /**
     * 默认jwt的过期时间
     */
    private static Long defaultExpiredDate;

    public JwtTokenUtil(String jwtSecret, String jwtIssuer,String jwtAudience, Long defaultExpiredDate) {
        this.jwtSecret = jwtSecret;
        this.jwtIssuer = jwtIssuer;
        this.jwtAudience = jwtAudience;
        this.defaultExpiredDate = defaultExpiredDate;
        //this.jwtProperties.setSecret(jwtSecret);
        //this.jwtProperties.setIssuer(jwtIssuer);
        //this.jwtProperties.setAudience(jwtAudience);
        //this.jwtProperties.setExpiration(defaultExpiredDate);
    }

    /**
     * 获取用户名从token中
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接收者
     */
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确(true-正确, false-错误)<br>
     */
    public Boolean checkToken(String token) throws JwtException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(userId,claims);
    }

    /**
     * 生成token,根据userId和默认过期时间
     */
    public String generateToken(String userId, Map<String, Object> claims) {
        final Date expirationDate = new Date(System.currentTimeMillis() + defaultExpiredDate * 1000);
        return generateToken(userId, expirationDate, claims);
    }

    /**
     * 生成token,根据userId和过期时间
     */
    public String generateToken(String userId, Date exppiredDate, Map<String, Object> claims) {
        final Date createdDate = new Date();
        if (claims == null) {
            return Jwts.builder()
                    //.setIssuer(jwtIssuer)
                    //.setAudience(jwtAudience)
                    .setSubject(userId)
                    .setIssuedAt(createdDate)
                    .setExpiration(exppiredDate)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        } else {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId)
                    .setIssuedAt(createdDate)
                    .setExpiration(exppiredDate)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }
    }
}