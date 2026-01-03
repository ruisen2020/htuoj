package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.TrainingProblemAddListReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.TrainingProblemAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemDeleteReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemGetProblemListReqDTO;
import org.example.htuoj.common.dto.resp.TrainingProblemGetProblemListRespDTP;
import org.example.htuoj.project.service.ITrainingProblemService;

/**
 * <p>
 * 训练题单与题目关联表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@RestController
@RequestMapping("/training-problem")
public class TrainingProblemController {
    @Autowired
    private ITrainingProblemService trainingProblemService;

    @PostMapping("getProblemListByTrainingId")
    public Result<IPage<TrainingProblemGetProblemListRespDTP>> getProblemListByTrainingId(@RequestBody TrainingProblemGetProblemListReqDTO reqDTO) {
        return Results.success(trainingProblemService.getProblemListByTrainingId(reqDTO));
    }

    @PostMapping("updateTrainingProblemList")
    @SaCheckLogin
    public Result<Void> updateTrainingProblemList(@RequestBody TrainingProblemAddListReqDTO reqDTO) {
        trainingProblemService.updateTrainingProblemList(reqDTO);
        return Results.success();
    }

    @PostMapping("addTrainingProblem")
    @SaCheckLogin
    public Result<Void> addTrainingProblem(@RequestBody TrainingProblemAddReqDTO reqDTO) {
        trainingProblemService.addTrainingProblem(reqDTO);
        return Results.success();
    }

    @PostMapping("deleteTrainingProblem")
    @SaCheckLogin
    public Result<Void> deleteTrainingProblem(@RequestBody TrainingProblemDeleteReqDTO reqDTO) {
        trainingProblemService.deleteTrainingProblem(reqDTO);
        return Results.success();
    }
}
