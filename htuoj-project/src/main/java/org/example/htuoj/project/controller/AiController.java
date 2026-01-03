package org.example.htuoj.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final Generation generation;

    @Value("${ai.api.key}")
    private String appKey;

    @Autowired
    public AiController(Generation generation) {
        this.generation = generation;
    }

    @PostMapping("/send")
    public Flux<ServerSentEvent<String>> aiTalk(@RequestBody String question, HttpServletResponse response) throws NoApiKeyException, InputRequiredException {
        System.out.println("question:" + question);
        // 构建ai对象
        Message message = Message.builder().role(Role.USER.getValue()).content(question).build();
        // 构建通义千问推送消息对象
        GenerationParam qwenParam = GenerationParam.builder()
                // 设置通义千问的类型模型
                .model("qwq-32b-preview")
                // 转化为一个新的List集合
                .messages(Arrays.asList(message))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                // 是否联网进行查询
                .apiKey(appKey)
                .build();
        // 执行方法获取流式返回数据
        Flowable<GenerationResult> result = generation.streamCall(qwenParam);
        return Flux.from(result).map(m -> {
                    // GenerationResult对象中输出流(GenerationOutput)的choices是一个列表，存放着生成的数据。
                    String content = m.getOutput().getChoices().get(0).getMessage().getContent();
                    System.out.println(content);
                    return ServerSentEvent.<String>builder().data(content).build();
                }).publishOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", "400");
                    map.put("message", "has mistake " + e.getMessage());
                    try {
                        // 设置流式处理webFlux
                        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
                        response.setCharacterEncoding("UTF-8");
                        response.getOutputStream().print(JsonUtils.toJson(map));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
    }


}
