package org.example.htuoj.judge.controller;

import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.JudgeTestReqDTO;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;
import org.example.htuoj.judge.service.IJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/judge")
public class JudgeController {


    @Autowired
    private IJudgeService judgeService;

    @PostMapping("testJudge")
    public Result<JudgeTestRespDTO> testJudge(@RequestBody JudgeTestReqDTO reqDTO) throws ExecutionException, InterruptedException {
        return Results.success(judgeService.testJudge(reqDTO));
    }
}
