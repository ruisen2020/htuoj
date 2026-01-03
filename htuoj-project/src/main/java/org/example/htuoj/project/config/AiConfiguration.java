package org.example.htuoj.project.config;

import com.alibaba.dashscope.aigc.generation.Generation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public Generation generation() {
        return new Generation();
    }
}