package org.example.htuoj.judge.service.impl;

import org.example.htuoj.common.convention.exception.ClientException;


import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dao.Submission;
import org.example.htuoj.common.dto.req.JudgeTestReqDTO;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;

import org.example.htuoj.common.mapper.ProblemMapper;
import org.example.htuoj.common.mapper.SubmissionMapper;
import org.example.htuoj.judge.service.IJudgeService;
import org.example.htuoj.judge.utils.JudgeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class JudgeServiceImpl implements IJudgeService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private JudgeUtils judgeUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void judge(String submissionId) throws IOException {
        // 1. 获取提交记录
        Submission submission = submissionMapper.selectById(submissionId);
        if (submission == null) {
            throw new ClientException("提交记录不存在");
        }
        // 2.查询题目信息
        Long problemId = submission.getProblemId();
        Problem problem = problemMapper.selectById(problemId);
        // 3. 开始评测
        Submission judgeResult = judgeUtils.judgeTask(submission, problem);
        // 5. 更新提交记录
        int ok = submissionMapper.updateById(judgeResult);
        if (ok != 1) {
            throw new ClientException("更新提交记录状态失败");
        }
        // 加入消息队列中
        // 队列名称
        String queueName = "accept.queue";
        // 消息
        String message = submission.getSubmissionId().toString();
        // 发送消息,只需要知道ID是谁就行了
        rabbitTemplate.convertAndSend(queueName, message);
        System.out.println("发送消息" + message);
    }

    @Override
    public JudgeTestRespDTO testJudge(JudgeTestReqDTO reqDTO) throws ExecutionException, InterruptedException {
        // 1. 获取提交记录
        Submission submission = reqDTO.getSubmission();
        if (submission == null) {
            throw new ClientException("提交记录不存在");
        }
        // 2.查询题目信息
        Long problemId = submission.getProblemId();
        Problem problem = problemMapper.selectById(problemId);
        // 3. 开始评测
        JudgeTestRespDTO result = judgeUtils.testJudge(submission, problem, reqDTO.getSampleList());
        return result;
    }
}
