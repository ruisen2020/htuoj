package org.example.htuoj.common.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.example.htuoj.common.fallback.JudgeFallbackFactory;
import org.example.htuoj.common.utils.UserHolder;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取登录用户
                Long userId = UserHolder.getUserId();
                if (userId == null) {
                    // 如果为空则直接跳过
                    return;
                }
                // 如果不为空则放入请求头中，传递给下游微服务
                template.header("userId", userId.toString());
            }
        };
    }

    @Bean
    public JudgeFallbackFactory fallbackFactory() {
        return new JudgeFallbackFactory();
    }
}