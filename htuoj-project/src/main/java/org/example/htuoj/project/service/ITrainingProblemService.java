package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.TrainingProblem;
import org.example.htuoj.common.dto.req.TrainingProblemAddListReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemDeleteReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemGetProblemListReqDTO;
import org.example.htuoj.common.dto.resp.TrainingProblemGetProblemListRespDTP;

/**
 * <p>
 * 训练题单与题目关联表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
public interface ITrainingProblemService extends IService<TrainingProblem> {

    IPage<TrainingProblemGetProblemListRespDTP> getProblemListByTrainingId(TrainingProblemGetProblemListReqDTO reqDTO);

    void addTrainingProblem(TrainingProblemAddReqDTO reqDTO);

    void deleteTrainingProblem(TrainingProblemDeleteReqDTO reqDTO);

    void updateTrainingProblemList(TrainingProblemAddListReqDTO reqDTO);
}
