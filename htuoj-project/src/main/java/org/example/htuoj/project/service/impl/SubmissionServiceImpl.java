package org.example.htuoj.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.client.JudgeClient;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.dao.Contest;
import org.example.htuoj.common.dao.ContestRegister;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;
import org.example.htuoj.common.mapper.ContestRegisterMapper;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.project.service.IContestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.Submission;
import org.example.htuoj.common.mapper.SubmissionMapper;
import org.example.htuoj.project.service.ISubmissionService;

import java.util.Date;

/**
 * <p>
 * 用户提交记录表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-20
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IContestService contestService;

    @Autowired
    private JudgeClient judgeClient;

    @Autowired
    private ContestRegisterMapper contestRegisterMapper;

    @Override
    public IPage<SubmissionGetListRespDTO> getSubmissionList(SubmissionGetListReqDTO reqDTO) {
        IPage<SubmissionGetListRespDTO> result = submissionMapper.getSubmissionList(reqDTO);
        return result;
    }

    @Override
    public SubmissionAddRespDTO addSubmission(SubmissionAddReqDTO reqDTO) {
        if (reqDTO.getContestId() != null) {
            Contest contest = contestService.getById(reqDTO.getContestId());
            Date now = new Date();
            Date startTime = contest.getStartTime();
            Date endTime = contest.getEndTime();
            if (now.before(startTime)) {
                throw new ClientException("比赛尚未开始，不允许提交");
            } else if (now.after(endTime)) {

            } else {
                // 检查是否报名
                LambdaQueryWrapper<ContestRegister> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ContestRegister::getUserId, UserHolder.getUserId());
                wrapper.eq(ContestRegister::getContestId, reqDTO.getContestId());
                if (contestRegisterMapper.selectOne(wrapper) == null) {
                    throw new ClientException("您未报名该比赛，不允许提交");
                }
            }
        }
        // 在数据库中加入这个提交记录
        Submission submission = BeanUtil.toBean(reqDTO, Submission.class);
        submission.setUserId(UserHolder.getUserId());
        System.out.println(reqDTO);
        System.out.println(submission);
        try {
            submissionMapper.insert(submission);
        } catch (Exception e) {
            throw new ClientException("提交失败");
        }
        //    提交成功，返回提交记录id
        Long submissionId = submission.getSubmissionId();
        // 加入消息队列中
        // 队列名称
        String queueName = "judge.queue";
        // 消息
        String message = submissionId.toString();
        // 发送消息,只需要知道ID是谁就行了
        rabbitTemplate.convertAndSend(queueName, message);
        SubmissionAddRespDTO result = new SubmissionAddRespDTO();
        result.setSubmissionId(submissionId);
        return result;
    }

    @Override
    public IPage<SubmissionGetListByProblemIdRespDTO> getSubmissionListByProblemId(SubmissionGetListByProblemIdReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(reqDTO.getUserId() != null, Submission::getUserId, reqDTO.getUserId());
        wrapper.eq(reqDTO.getStatus() != null, Submission::getStatus, reqDTO.getStatus());
        wrapper.eq(reqDTO.getLanguage() != null, Submission::getLanguage, reqDTO.getLanguage());
        wrapper.eq(Submission::getProblemId, reqDTO.getProblemId());
        wrapper.orderByDesc(Submission::getCreateTime);
        IPage<Submission> submissionIPage = submissionMapper.selectPage(reqDTO, wrapper);
        IPage<SubmissionGetListByProblemIdRespDTO> result = submissionIPage.convert(submission -> BeanUtil.toBean(submission, SubmissionGetListByProblemIdRespDTO.class));
        result.setTotal(submissionIPage.getTotal());
        result.setPages(submissionIPage.getPages());
        result.setCurrent(submissionIPage.getCurrent());
        result.setSize(submissionIPage.getSize());
        return result;
    }

    @Override
    public SubmissionGetByIdRespDTO getSubmissionById(SubmissionGetByIdReqDTO reqDTO) {
        Submission submission = this.getById(reqDTO.getSubmissionId());
        SubmissionGetByIdRespDTO submissionGetByIdRespDTO = BeanUtil.toBean(submission, SubmissionGetByIdRespDTO.class);
        return submissionGetByIdRespDTO;
    }

    @Override
    public SubmissionGetLastByProblemIdRespDTO getLastSubmissionByProblemId(SubmissionGetLastByProblemIdReqDTO reqDTO) {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getProblemId, reqDTO.getProblemId());
        wrapper.eq(Submission::getUserId, UserHolder.getUserId());
        wrapper.orderByDesc(Submission::getCreateTime);
        wrapper.last("limit 1");
        Submission submission = submissionMapper.selectOne(wrapper);
        SubmissionGetLastByProblemIdRespDTO respDTO = new SubmissionGetLastByProblemIdRespDTO();
        if (submission == null) {
            respDTO.setCode("");
            respDTO.setLanguage(1);
        } else {
            respDTO.setCode(submission.getCode());
            respDTO.setLanguage(submission.getLanguage());
        }
        return respDTO;
    }

    @Override
    public Result<JudgeTestRespDTO> addTestSubmission(SubmissionAddTestReqDTO reqDTO) {
        // 在数据库中加入这个提交记录
        Submission submission = BeanUtil.toBean(reqDTO, Submission.class);
        submission.setUserId(UserHolder.getUserId());
//        try {
//            submissionMapper.insert(submission);
//        } catch (Exception e) {
//            throw new ClientException("提交失败");
//        }
        JudgeTestReqDTO judgeTestReqDTO = new JudgeTestReqDTO();
        judgeTestReqDTO.setSubmission(submission);
        judgeTestReqDTO.setSampleList(reqDTO.getSampleList());
        Result<JudgeTestRespDTO> result = judgeClient.testJudge(judgeTestReqDTO);
        System.out.println(result);
        return result;
    }

    @Override
    public IPage<SubmissionGetListRespDTO> getSubmissionListByContestId(SubmissionGetListReqDTO reqDTO) {
        IPage<SubmissionGetListRespDTO> result = submissionMapper.getSubmissionListByContestId(reqDTO);
        return result;
    }
}
