package org.example.htuoj.judge.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class RabbitConfig {
 
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        // 设置队列的名称
//        template.setQueue("myQueue");
//        // 如果需要，可以设置交换器和路由键
//        template.setExchange("myExchange");
//        template.setRoutingKey("myRoutingKey");
//        return template;
//    }
 
    // 如果需要手动管理队列，可以注册一个RabbitAdmin
//    @Bean
//    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }
}