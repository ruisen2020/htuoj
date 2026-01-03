package org.example.htuoj.judge;

import org.example.htuoj.common.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.htuoj.common.client", defaultConfiguration = DefaultFeignConfig.class)
@ComponentScan(basePackages = {"org.example.htuoj.common", "org.example.htuoj.judge"})
@MapperScan("org.example.htuoj.common.mapper")
public class JudgeApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(JudgeApplication.class, args);
    }
}
