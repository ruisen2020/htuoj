package org.example.htuoj.project.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dao.Training;
import org.example.htuoj.common.dto.req.TrainingProblemAddListReqDTO;
import org.example.htuoj.common.mapper.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.TrainingProblem;
import org.example.htuoj.common.dto.req.TrainingProblemAddReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemDeleteReqDTO;
import org.example.htuoj.common.dto.req.TrainingProblemGetProblemListReqDTO;
import org.example.htuoj.common.dto.resp.TrainingProblemGetProblemListRespDTP;
import org.example.htuoj.common.mapper.TrainingProblemMapper;
import org.example.htuoj.project.service.ITrainingProblemService;
import org.example.htuoj.common.utils.UserHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 训练题单与题目关联表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@Service
public class TrainingProblemServiceImpl extends ServiceImpl<TrainingProblemMapper, TrainingProblem> implements ITrainingProblemService {
    @Autowired
    private TrainingProblemMapper trainingProblemMapper;
    @Autowired
    private TrainingMapper trainingMapper;

    @Override
    public IPage<TrainingProblemGetProblemListRespDTP> getProblemListByTrainingId(TrainingProblemGetProblemListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        IPage<TrainingProblemGetProblemListRespDTP> result = trainingProblemMapper.getProblemListByTrainingId(reqDTO);
        for (TrainingProblemGetProblemListRespDTP item : result.getRecords()) {
            double passRate = 0.0;
            if (item.getSubmitCount() != 0)
                passRate = (double) item.getAcceptCount() / item.getSubmitCount() * 100.0;
            String passRateStr = String.format("%.1f", passRate) + "%";
            item.setPassRate(passRateStr);
        }
        result.getRecords().sort((o1, o2) -> o1.getOrder() - o2.getOrder());
        return result;
    }

    @Override
    @SaCheckLogin
    public void addTrainingProblem(TrainingProblemAddReqDTO reqDTO) {
        LambdaQueryWrapper<TrainingProblem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainingProblem::getTrainingId, reqDTO.getTrainingId());
        wrapper.eq(TrainingProblem::getProblemId, reqDTO.getProblemId());
        TrainingProblem one = trainingProblemMapper.selectOne(wrapper);
        if (one != null) {
            one.setDelFlag(0);
            int update = trainingProblemMapper.updateById(one);
            if (update != 1) {
                throw new ClientException("删除题目失败");
            }
            return;
        }
        TrainingProblem trainingProblem = new TrainingProblem();
        trainingProblem.setProblemId(reqDTO.getProblemId());
        trainingProblem.setTrainingId(reqDTO.getTrainingId());
        int insert = trainingProblemMapper.insert(trainingProblem);
        if (insert != 1) {
            throw new RuntimeException("添加题目失败");
        }
    }

    @Override
    @SaCheckLogin
    public void deleteTrainingProblem(TrainingProblemDeleteReqDTO reqDTO) {
        LambdaQueryWrapper<Training> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Training::getTrainingId, reqDTO.getTrainingId());
        Training one = trainingMapper.selectOne(queryWrapper);
        if (!Objects.equals(one.getUserId(), UserHolder.getUserId())) {
            throw new ClientException("没有权限删除该题单");
        }
        LambdaUpdateWrapper<TrainingProblem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TrainingProblem::getTrainingId, reqDTO.getTrainingId());
        updateWrapper.eq(TrainingProblem::getProblemId, reqDTO.getProblemId());
        updateWrapper.set(TrainingProblem::getDelFlag, 1);
        int update = trainingProblemMapper.update(null, updateWrapper);
        if (update != 1) {
            throw new ClientException("删除题目失败");
        }
    }

    @Override
    @Transactional
    public void updateTrainingProblemList(TrainingProblemAddListReqDTO reqDTO) {
        Training training = trainingMapper.selectById(reqDTO.getTrainingId());
        if (training == null) {
            throw new ClientException("题单不存在");
        }
        if (!training.getUserId().equals(UserHolder.getUserId())) {
            throw new ClientException("没有权限修改该题单");
        }
        training.setProblemCount(reqDTO.getProblemIds().size());
        trainingMapper.updateById(training);
        LambdaQueryWrapper<TrainingProblem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainingProblem::getTrainingId, reqDTO.getTrainingId());
        List<TrainingProblem> trainingProblemList = trainingProblemMapper.selectList(wrapper);
        HashMap<Long, TrainingProblem> longTrainingProblemHashMap = new HashMap<>();
        for (int i = 0; i < trainingProblemList.size(); i++) {
            TrainingProblem trainingProblem = trainingProblemList.get(i);
            longTrainingProblemHashMap.put(trainingProblem.getProblemId(), trainingProblem);
        }
        List<Long> problemIds = reqDTO.getProblemIds();
        for (int i = 0; i < problemIds.size(); i++) {
            Long problemId = problemIds.get(i);
            if (!longTrainingProblemHashMap.containsKey(problemId)) {
                TrainingProblem trainingProblem = new TrainingProblem();
                trainingProblem.setTrainingId(reqDTO.getTrainingId());
                trainingProblem.setProblemId(problemId);
                trainingProblem.setOrder(i + 1);
                trainingProblemMapper.insert(trainingProblem);
            } else {
                TrainingProblem trainingProblem = longTrainingProblemHashMap.get(problemId);
                trainingProblem.setOrder(i + 1);
                trainingProblemMapper.updateById(trainingProblem);
                longTrainingProblemHashMap.remove(problemId);
            }
        }
        for (Long problemId : longTrainingProblemHashMap.keySet()) {
            TrainingProblem trainingProblem = longTrainingProblemHashMap.get(problemId);
            this.removeById(trainingProblem.getTrainingProblemId());
            trainingProblemMapper.updateById(trainingProblem);
        }
    }
}
