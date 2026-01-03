package org.example.htuoj.project.config;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://117.72.110.81:6379")
                .setPassword("9Xd5wecCJkqpJPmvwtXs3HRdF7Jcfe4d");

        // 创建RedissonClient对象
        return Redisson.create(config);
    }

    @Bean
    public RBloomFilter<String> userNameBloomFilter(RedissonClient redissonClient) {
        // 使用Redisson客户端创建RBloomFilter实例
        RBloomFilter<String> userNameBloomFilter = redissonClient.getBloomFilter("userNameBloomFilter");
        userNameBloomFilter.tryInit(1000000, 0.01); // 初始化布隆过滤器，预计插入100000000个元素，错误率为0.03
        return  userNameBloomFilter;
    }

    @Bean
    public RBloomFilter<String> numberBloomFilter(RedissonClient redissonClient) {
        // 使用Redisson客户端创建RBloomFilter实例
        RBloomFilter<String> numberBloomFilter = redissonClient.getBloomFilter("numberBloomFilter");
        numberBloomFilter.tryInit(1000000, 0.01); // 初始化布隆过滤器，预计插入100000000个元素，错误率为0.03
        return  numberBloomFilter;
    }
}
