package org.example.htuoj.project.utils;

import com.aliyun.dm20151123.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailConfig {

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 发信地址
     */
    private String accountName;

    /**
     * 发信地址类型
     */
    private Integer addressType;

    /**
     * 发信人昵称
     */
    private String fromAlias;

    /**
     * 邮件主题
     */
    private String subject;



}

