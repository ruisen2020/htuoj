package org.example.htuoj.common.fallback;

import org.example.htuoj.common.client.JudgeClient;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.dto.req.JudgeTestReqDTO;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

public class JudgeFallbackFactory implements FallbackFactory<JudgeClient> {
    @Override
    public JudgeClient create(Throwable cause) {
        return new JudgeClient() {

            @Override
            public Result<JudgeTestRespDTO> testJudge(JudgeTestReqDTO reqDTO) {
                return null;
            }
        };
    }
}
