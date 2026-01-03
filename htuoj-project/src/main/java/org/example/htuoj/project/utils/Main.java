package org.example.htuoj.project.utils;

import java.io.IOException;
import java.util.Arrays;
import java.lang.System;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import io.reactivex.Flowable;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Main {
    public static GenerationResult callWithMessage() throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("给我写一篇水浒传文章")
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-2849bedf6f9a42a3b85ecc53a2996d9b")
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwq-32b-preview")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        // 执行方法获取流式返回数据
        Generation generation = new Generation();
        Flowable<GenerationResult> result = generation.streamCall(param);
        Flux.from(result).map(m -> {
                    // GenerationResult对象中输出流(GenerationOutput)的choices是一个列表，存放着生成的数据。
                    String content = m.getOutput().getChoices().get(0).getMessage().getContent();
                    return ServerSentEvent.<String>builder().data(content).build();
                }).publishOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", "400");
                    map.put("message", "has mistake " + e.getMessage());
                    try {
                        // 设置流式处理webFlux
                        System.out.println(JsonUtils.toJson(map));
//                        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
//                        response.setCharacterEncoding("UTF-8");
//                        response.getOutputStream().print(JsonUtils.toJson(map));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
        return gen.call(param);
    }

    public static void main(String[] args) {
        try {
            GenerationResult result = callWithMessage();
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("错误信息：" + e.getMessage());
            System.out.println("请参考文档：https://help.aliyun.com/zh/model-studio/developer-reference/error-code");
        }
        System.exit(0);
    }
}