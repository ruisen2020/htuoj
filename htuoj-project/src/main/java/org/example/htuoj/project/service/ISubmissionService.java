package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.dao.Submission;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;

/**
 * <p>
 * 用户提交记录表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-20
 */
public interface ISubmissionService extends IService<Submission> {

    IPage<SubmissionGetListRespDTO> getSubmissionList(SubmissionGetListReqDTO reqDTO);

    SubmissionAddRespDTO addSubmission(SubmissionAddReqDTO reqDTO);

    IPage<SubmissionGetListByProblemIdRespDTO> getSubmissionListByProblemId(SubmissionGetListByProblemIdReqDTO reqDTO);

    SubmissionGetByIdRespDTO getSubmissionById(SubmissionGetByIdReqDTO reqDTO);

    SubmissionGetLastByProblemIdRespDTO getLastSubmissionByProblemId(SubmissionGetLastByProblemIdReqDTO reqDTO);

    Result<JudgeTestRespDTO> addTestSubmission(SubmissionAddTestReqDTO reqDTO);

    IPage<SubmissionGetListRespDTO> getSubmissionListByContestId(SubmissionGetListReqDTO reqDTO);
}
