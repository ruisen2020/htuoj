package org.example.htuoj.judge.service;

import org.example.htuoj.common.dto.req.JudgeTestReqDTO;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IJudgeService {
    void judge(String submissionId) throws IOException;

    JudgeTestRespDTO testJudge(JudgeTestReqDTO reqDTO) throws ExecutionException, InterruptedException;
}
