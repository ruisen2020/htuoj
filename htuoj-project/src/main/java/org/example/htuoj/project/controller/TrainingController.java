package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.TrainingAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetOfficialTrainingListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetTrainingInfoReqDTO;
import org.example.htuoj.common.dto.resp.TrainingGetTrainingInfoRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.resp.TrainingGetListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetOfficialTrainingListRespDTO;
import org.example.htuoj.project.service.ITrainingService;

import java.util.List;

/**
 * <p>
 * 训练题单表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@RestController
@RequestMapping("/training")
public class TrainingController {
    @Autowired
    private ITrainingService trainingService;

    @PostMapping("/addTraining")
    @SaCheckLogin
    public Result<Void> addTraining(@RequestBody TrainingAddReqDTO reqDTO) {
        return Results.success(trainingService.addTraining(reqDTO));
    }

    @PostMapping("/deleteTraining")
    @SaCheckLogin
    public Result<Void> deleteTraining(@RequestBody TrainingAddReqDTO reqDTO) {
        return Results.success(trainingService.deleteTraining(reqDTO));
    }

    @PostMapping("/updateTraining")
    @SaCheckLogin
    public Result<Void> updateTraining(@RequestBody TrainingAddReqDTO reqDTO) {
        return Results.success(trainingService.updateTraining(reqDTO));
    }

    /**
     * 获取官方题单列表或者用户分享题单列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("getOfficialTrainingList")
    public Result<IPage<TrainingGetOfficialTrainingListRespDTO>> getOfficialTrainingList(@RequestBody TrainingGetOfficialTrainingListReqDTO reqDTO) {
        return Results.success(trainingService.getOfficialTrainingList(reqDTO));
    }

    /**
     * 获取个人题单列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/getTrainingList")
    public Result<List<TrainingGetListRespDTO>> getTrainingList(@RequestBody TrainingGetListReqDTO reqDTO) {
        return Results.success(trainingService.getTrainingList(reqDTO));
    }

    @PostMapping("/getTrainingListByCollect")
    public Result<List<TrainingGetListRespDTO>> getTrainingListByCollect(@RequestBody TrainingGetListReqDTO reqDTO) {

        return Results.success(trainingService.getTrainingListByCollect(reqDTO));
    }

    @PostMapping("/getTrainingInfo")
    public Result<TrainingGetTrainingInfoRespDTO> getTrainingInfo(@RequestBody TrainingGetTrainingInfoReqDTO reqDTO) {
        return Results.success(trainingService.getTrainingInfo(reqDTO));
    }
}
