package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dto.req.TrainingGetTrainingInfoReqDTO;
import org.example.htuoj.common.dto.resp.TrainingGetTrainingInfoRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Training;
import org.example.htuoj.common.dto.req.TrainingAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetListReqDTO;
import org.example.htuoj.common.dto.req.TrainingGetOfficialTrainingListReqDTO;
import org.example.htuoj.common.dto.resp.TrainingGetListRespDTO;
import org.example.htuoj.common.dto.resp.TrainingGetOfficialTrainingListRespDTO;
import org.example.htuoj.common.mapper.TrainingMapper;
import org.example.htuoj.project.service.ITrainingService;
import org.example.htuoj.common.utils.UserHolder;

import java.util.List;

/**
 * <p>
 * 训练题单表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@Service
public class TrainingServiceImpl extends ServiceImpl<TrainingMapper, Training> implements ITrainingService {
    @Autowired
    private TrainingMapper trainingMapper;

    @Override
    public List<TrainingGetListRespDTO> getTrainingList(TrainingGetListReqDTO reqDTO) {
        reqDTO.setMyUserId(UserHolder.getUserId());
        List<TrainingGetListRespDTO> result = trainingMapper.getTrainingList(reqDTO);
        return result;
    }

    @Override
    public IPage<TrainingGetOfficialTrainingListRespDTO> getOfficialTrainingList(TrainingGetOfficialTrainingListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        // 每一个题单的所有题目，然后在查该用户已经完成了多少题目, 然后返回给前端
        IPage<TrainingGetOfficialTrainingListRespDTO> result = trainingMapper.getOfficialTrainingList(reqDTO);
        for (TrainingGetOfficialTrainingListRespDTO dto : result.getRecords()) {
            Integer completionRate = dto.getCompletionRate();
            if (completionRate == null || completionRate == 0) {
                dto.setCompletionRate(0);
            } else {
                Integer problemCount = dto.getProblemCount();
                Integer rate = Math.toIntExact(Math.round((double) completionRate / problemCount * 100));
                dto.setCompletionRate(rate);
            }
        }
        return result;
    }

    @Override
    public Void addTraining(TrainingAddReqDTO reqDTO) {
        Training training = new Training();
        training.setUserId(UserHolder.getUserId());
        training.setTitle(reqDTO.getTitle());
        training.setDescription(reqDTO.getDescription());
        try {
            trainingMapper.insert(training);
        } catch (Exception e) {
            throw new ClientException("创新题单失败");
        }
        return null;
    }

    @Override
    public List<TrainingGetListRespDTO> getTrainingListByCollect(TrainingGetListReqDTO reqDTO) {
        reqDTO.setMyUserId(UserHolder.getUserId());
        return trainingMapper.getTrainingListByCollect(reqDTO);
    }

    @Override
    public TrainingGetTrainingInfoRespDTO getTrainingInfo(TrainingGetTrainingInfoReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        TrainingGetTrainingInfoRespDTO result = trainingMapper.getTrainingInfo(reqDTO);
        Integer completionRate = result.getCompletionRate();
        if (completionRate == null || completionRate == 0) {
            result.setCompletionRate(0);
        } else {
            Integer problemCount = result.getProblemCount();
            Integer rate = Math.toIntExact(Math.round((double) completionRate / problemCount * 100));
            result.setCompletionRate(rate);
        }
        return result;
    }

    @Override
    public Void updateTraining(TrainingAddReqDTO reqDTO) {
        Training byId = this.getById(reqDTO.getTrainingId());
        if (byId == null) {
            throw new ClientException("题单不存在");
        }
        if (!byId.getUserId().equals(UserHolder.getUserId())) {
            throw new ClientException("没有权限修改该题单");
        }
        LambdaUpdateWrapper<Training> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Training::getTrainingId, reqDTO.getTrainingId());
        updateWrapper.set(Training::getTitle, reqDTO.getTitle());
        updateWrapper.set(Training::getDescription, reqDTO.getDescription());
        try {
            trainingMapper.update(null, updateWrapper);
        } catch (Exception e) {
            throw new ClientException("更新题单失败");
        }
        return null;
    }

    @Override
    public Void deleteTraining(TrainingAddReqDTO reqDTO) {
        Training byId = this.getById(reqDTO.getTrainingId());
        if (byId == null) {
            throw new ClientException("题单不存在");
        }
        if (!byId.getUserId().equals(UserHolder.getUserId())) {
            throw new ClientException("没有权限修改该题单");
        }
        LambdaUpdateWrapper<Training> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Training::getTrainingId, reqDTO.getTrainingId());
        updateWrapper.setSql("del_flag = 1");
        try {
            trainingMapper.update(null, updateWrapper);
        } catch (Exception e) {
            throw new ClientException("更新题单失败");
        }
        return null;
    }


}
