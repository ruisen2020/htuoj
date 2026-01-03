package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.project.service.ISubmissionService;

/**
 * <p>
 * 用户提交记录表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-20
 */
@RestController
@RequestMapping("/submission")
public class  SubmissionController {

    @Autowired
    private ISubmissionService submissionService;



    /**
     * 获取提交列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("getSubmissionList")
    public Result<IPage<SubmissionGetListRespDTO>> getSubmissionList(@RequestBody SubmissionGetListReqDTO reqDTO) {
        return Results.success(submissionService.getSubmissionList(reqDTO));
    }

    @PostMapping("getSubmissionListByContestId")
    public Result<IPage<SubmissionGetListRespDTO>> getSubmissionListByContestId(@RequestBody SubmissionGetListReqDTO reqDTO) {
        return Results.success(submissionService.getSubmissionListByContestId(reqDTO));
    }

    @PostMapping("/addSubmission")
    @SaCheckLogin
    public Result<SubmissionAddRespDTO> addSubmission(@RequestBody SubmissionAddReqDTO reqDTO) {
        return Results.success(submissionService.addSubmission(reqDTO));
    }

    @PostMapping("getSubmissionListByProblemId")
    @SaCheckLogin
    public Result<IPage<SubmissionGetListByProblemIdRespDTO>> getSubmissionListByProblemId(@RequestBody SubmissionGetListByProblemIdReqDTO reqDTO) {
        return Results.success(submissionService.getSubmissionListByProblemId(reqDTO));
    }

    @PostMapping("getSubmissionById")
    public Result<SubmissionGetByIdRespDTO> getSubmissionById(@RequestBody SubmissionGetByIdReqDTO reqDTO) {
        return Results.success(submissionService.getSubmissionById(reqDTO));
    }

    @PostMapping("getLastSubmissionByProblemId")
    public Result<SubmissionGetLastByProblemIdRespDTO> getLastSubmissionByProblemId(@RequestBody SubmissionGetLastByProblemIdReqDTO reqDTO) {
        return Results.success(submissionService.getLastSubmissionByProblemId(reqDTO));
    }

    @PostMapping("addTestSubmission")
    @SaCheckLogin
    public Result<JudgeTestRespDTO> addTestSubmission(@RequestBody SubmissionAddTestReqDTO reqDTO) {
        return submissionService.addTestSubmission(reqDTO);
    }

}
