package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Training;
import org.example.htuoj.common.dto.req.TrainingGetListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetOfficialTrainingListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetTrainingInfoReqDTO;
import org.example.htuoj.common.dto.resp.TrainingGetListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetOfficialTrainingListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetTrainingInfoRespDTO;

import java.util.List;

/**
 * <p>
 * 训练题单表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
public interface TrainingMapper extends BaseMapper<Training> {

    IPage<TrainingGetOfficialTrainingListRespDTO> getOfficialTrainingList(TrainingGetOfficialTrainingListReqDTO reqDTO);

    List<TrainingGetListRespDTO> getTrainingList(TrainingGetListReqDTO reqDTO);

    List<TrainingGetListRespDTO> getTrainingListByCollect(TrainingGetListReqDTO reqDTO);

    TrainingGetTrainingInfoRespDTO getTrainingInfo(TrainingGetTrainingInfoReqDTO reqDTO);
}
