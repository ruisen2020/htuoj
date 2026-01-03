package org.example.htuoj.gateway.filter;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.htuoj.gateway.config.AuthProperties;
import org.example.htuoj.gateway.util.CollUtils;
import org.example.htuoj.gateway.util.JwtUtils;
import org.example.htuoj.gateway.util.RedisConstant;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;

    private final StringRedisTemplate stringRedisTemplate;

    private final AuthProperties authProperties;



    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("aaaaaaaaaaaaaaaaaaaa");
        // 1.获取Request
        ServerHttpRequest request = exchange.getRequest();
//        // 2.判断是否不需要拦截
//        if (isExclude(request.getPath().toString())) {
//            // 无需拦截，直接放行
//            return chain.filter(exchange);
//        }
//        String token = null;
//        List<String> headers = request.getHeaders().get("authorization");
//        if (!CollUtils.isEmpty(headers)) {
//            token = headers.get(0);
//        }
//        ServerHttpResponse response = exchange.getResponse();
//        response.setRawStatusCode(401);
//        if (StrUtil.isBlank(token)) {
//            return response.setComplete();
//        }
//        if (jwtUtils.getClaimByToken(token) == null) {
//            return response.setComplete();
//        }
//        String userId = jwtUtils.getClaimByToken(token).getSubject();
//        // 2.基于TOKEN获取redis中的用户
//        String key = RedisConstant.USER_TOKEN_PREFIX + userId;
//        String value = stringRedisTemplate.opsForValue().get(key);
//        // 3.判断用户是否存在
//        if (!token.equals(value)) {
////            System.out.println("token过期");
//            return response.setComplete();
//        }
//        stringRedisTemplate.expire(key, 60, TimeUnit.MINUTES);
        String userId = null;
        try {
            userId = StpUtil.getLoginIdAsString();
        } catch (Exception e) {
            return chain.filter(exchange);
        }

        String finalUserId = userId;
        ServerWebExchange ex = exchange.mutate()
                .request(b -> b.header("userId", finalUserId))
                .build();
        return chain.filter(ex);
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}