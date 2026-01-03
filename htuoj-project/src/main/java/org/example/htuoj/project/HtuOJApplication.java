package org.example.htuoj.project;


import org.example.htuoj.common.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.htuoj.common.client", defaultConfiguration = DefaultFeignConfig.class)
@ComponentScan(basePackages = {"org.example.htuoj.common", "org.example.htuoj.project"})
@MapperScan("org.example.htuoj.common.mapper")
public class HtuOJApplication {
    public static void main(String[] args) {
        String a="11";
        SpringApplication.run(HtuOJApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}