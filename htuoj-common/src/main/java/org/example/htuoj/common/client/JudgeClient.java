package org.example.htuoj.common.client;

import org.example.htuoj.common.config.DefaultFeignConfig;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.JudgeTestReqDTO;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;
import org.example.htuoj.common.fallback.JudgeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "htuoj-judge",
        configuration = DefaultFeignConfig.class,
        fallbackFactory = JudgeFallbackFactory.class)
public interface JudgeClient {
    @PostMapping("/judge/testJudge")
    Result<JudgeTestRespDTO> testJudge(@RequestBody JudgeTestReqDTO reqDTO);
}
