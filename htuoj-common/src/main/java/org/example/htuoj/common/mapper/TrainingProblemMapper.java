package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.TrainingProblem;
import org.example.htuoj.common.dto.req.TrainingProblemGetProblemListReqDTO;
import org.example.htuoj.common.dto.resp.TrainingProblemGetProblemListRespDTP;

/**
 * <p>
 * 训练题单与题目关联表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
public interface TrainingProblemMapper extends BaseMapper<TrainingProblem> {

    IPage<TrainingProblemGetProblemListRespDTP> getProblemListByTrainingId(TrainingProblemGetProblemListReqDTO reqDTO);
}
