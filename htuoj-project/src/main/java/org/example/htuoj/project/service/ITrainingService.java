package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Training;
import org.example.htuoj.common.dto.req.TrainingAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetOfficialTrainingListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetTrainingInfoReqDTO;
import org.example.htuoj.common.dto.resp.TrainingGetListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetOfficialTrainingListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetTrainingInfoRespDTO;

import java.util.List;

/**
 * <p>
 * 训练题单表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
public interface ITrainingService extends IService<Training> {

    List<TrainingGetListRespDTO> getTrainingList(TrainingGetListReqDTO reqDTO);

    IPage<TrainingGetOfficialTrainingListRespDTO> getOfficialTrainingList(TrainingGetOfficialTrainingListReqDTO reqDTO);

    Void addTraining(TrainingAddReqDTO reqDTO);

    List<TrainingGetListRespDTO> getTrainingListByCollect(TrainingGetListReqDTO reqDTO);

    TrainingGetTrainingInfoRespDTO getTrainingInfo(TrainingGetTrainingInfoReqDTO reqDTO);

    Void updateTraining(TrainingAddReqDTO reqDTO);

    Void deleteTraining(TrainingAddReqDTO reqDTO);
}
