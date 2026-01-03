package org.example.htuoj.judge.config;

import org.example.htuoj.judge.service.IJudgeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringRabbitListener {

    @Autowired
    private IJudgeService judgeService;

    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "judge.queue")
    public void listenSimpleQueueMessage(String submissionId) throws InterruptedException, IOException {
        // 收到消息，进行处理
        System.out.println("收到消息：" + submissionId);
        try {
            judgeService.judge(submissionId);
        } catch (Exception e) {
            throw new MessageConversionException("消息消费失败");
        }

    }
}