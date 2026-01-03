package org.example.htuoj.gateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class GlobalCorsConfig {
    @Value("${htuoj.ip}")
    private String ip;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 这里仅为了说明问题，配置为放行所有域名，生产环境请对此进行修改
        config.addAllowedOrigin("http://117.72.110.81");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://htuoj.cn");
        config.addAllowedOrigin("http://www.htuoj.cn");
        config.addAllowedOrigin("https://117.72.110.81");
        config.addAllowedOrigin("https://localhost:8080");
        config.addAllowedOrigin("https://htuoj.cn");
        config.addAllowedOrigin("https://www.htuoj.cn");

        // 放行的请求头
        config.addAllowedHeader("*");
        // 放行的请求类型，有 GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedMethod("*");
        // 暴露头部信息
        config.addExposedHeader("*");
        // 是否允许发送 Cookie
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}