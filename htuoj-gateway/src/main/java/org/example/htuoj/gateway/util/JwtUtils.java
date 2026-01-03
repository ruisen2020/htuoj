package org.example.htuoj.gateway.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    private String secret;

    private long expire;

    private String header;


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成jwt token
     */
    public String generateToken(String userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_TOKEN_PREFIX + userId, token, expire, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    public void cleanToken(String uid) {
        redisUtils.del(RedisConstant.USER_TOKEN_PREFIX + uid);
    }

    public boolean hasToken(String uid) {
        return redisUtils.hasKey(RedisConstant.USER_TOKEN_PREFIX + uid);
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }


}
