package org.example.htuoj.dispatch;

import org.example.htuoj.common.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients(basePackages = "org.example.htuoj.common.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages ={"org.example.htuoj.common", "org.example.htuoj.dispatch"})
public class HTUOJDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(HTUOJDispatchApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}