package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dao.ProblemLabelRelations;
import org.example.htuoj.common.dao.ProblemSourceRelations;
import org.example.htuoj.common.dao.Sample;
import org.example.htuoj.common.dto.req.ProblemAddReqDTO;
import org.example.htuoj.common.dto.req.ProblemPageReqDTO;
import org.example.htuoj.common.dto.req.ProblemUpdateReqDTO;
import org.example.htuoj.common.mapper.ProblemLabelRelationsMapper;
import org.example.htuoj.common.mapper.ProblemMapper;
import org.example.htuoj.common.mapper.SampleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;

import org.example.htuoj.common.dto.resp.ProblemGetByIdRespDTO;
import org.example.htuoj.common.dto.resp.ProblemPageRespDTO;
import org.example.htuoj.common.dto.resp.SampleInfoRespSTO;
import org.example.htuoj.project.enums.ProblemErrorCodeEnum;
import org.example.htuoj.project.service.IProblemLabelRelationsService;
import org.example.htuoj.project.service.IProblemService;
import org.example.htuoj.project.service.IProblemSourceRelationsService;
import org.example.htuoj.project.utils.LabelCollection;
import org.example.htuoj.common.utils.UserHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 题目表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemLabelRelationsMapper problemLabelRelationsMapper;

    @Autowired
    private IProblemLabelRelationsService iProblemLabelRelationsService;

    @Autowired
    private IProblemSourceRelationsService iProblemSourceRelationsService;

    @Autowired
    private SampleMapper sampleMapper;


    @Override
    @Transactional
    public Result<Void> add(ProblemAddReqDTO reqDTO) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(reqDTO, problem);
        // 插入
        int id = problemMapper.add(problem);
        if (id < 0) {
            throw new ClientException(ProblemErrorCodeEnum.Problem_SAVE_ERROR);
        }
        // 插入标签关联表
        List<String> labels = reqDTO.getLabels();
        ArrayList<ProblemLabelRelations> problemLabelRelationList = new ArrayList<>();
        for (String label : labels) {
            ProblemLabelRelations problemLabelRelations = new ProblemLabelRelations();
            problemLabelRelations.setProblemId(problem.getId());
            problemLabelRelations.setLabelId(Long.valueOf(label));
            problemLabelRelationList.add(problemLabelRelations);
        }
        boolean ok = iProblemLabelRelationsService.saveBatch(problemLabelRelationList);
        if (!ok) {
            throw new ClientException(ProblemErrorCodeEnum.Problem_SAVE_ERROR);
        }
        // 插入题目来源表
        List<String> sources = reqDTO.getSources();
        ArrayList<ProblemSourceRelations> problemSourceRelationList = new ArrayList<>();
        for (String source : sources) {
            ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
            problemSourceRelations.setProblemId(problem.getId());
            problemSourceRelations.setSourceId(Long.valueOf(source));
            problemSourceRelationList.add(problemSourceRelations);
        }
        boolean ok1 = iProblemSourceRelationsService.saveBatch(problemSourceRelationList);
        if (!ok1) {
            throw new ClientException(ProblemErrorCodeEnum.Problem_SAVE_ERROR);
        }
        return Results.success();
    }

    @Override
    @Transactional
    public synchronized void update(ProblemUpdateReqDTO reqDTO) {
        // TODO:更新题目需要修改标签相关
        Problem problem = new Problem();
        BeanUtils.copyProperties(reqDTO, problem);
        int result = problemMapper.updateById(problem);
        if (result < 0)
            throw new ClientException(ProblemErrorCodeEnum.Problem_UPDATE_ERROR);

        // 获取该题目原有的标签，在与更新的标签比较，如果新标签中有，原标签没有，就新增，如果原标签中有，新标签中没有，就删除，否则不处理
        LambdaQueryWrapper<ProblemLabelRelations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProblemLabelRelations::getProblemId, problem.getId());
        List<ProblemLabelRelations> list = problemLabelRelationsMapper.selectList(wrapper);
        HashSet<Long> oldLabels = new HashSet<>();
        for (ProblemLabelRelations problemLabelRelations : list) {
            oldLabels.add(problemLabelRelations.getLabelId());
        }
        List<Long> newLabels = LabelCollection.getLabelIds(reqDTO.getLabels());
        for (Long labelId : newLabels) {
            // 如果原标签中有，新标签中也有，不处理
            if (oldLabels.contains(labelId)) {
                oldLabels.remove(labelId);
            } else {
                // 如果新标签中有，原标签没有，就新增
                ProblemLabelRelations problemLabelRelations = new ProblemLabelRelations();
                problemLabelRelations.setProblemId(problem.getId());
                problemLabelRelations.setLabelId(labelId);
                int insert = problemLabelRelationsMapper.insert(problemLabelRelations);
                if (insert < 0) {
                    throw new ClientException(ProblemErrorCodeEnum.Problem_UPDATE_ERROR);
                }
            }
        }
        // 果原标签中有，新标签中没有，就删除
        for (Long labelId : oldLabels) {
            LambdaQueryWrapper<ProblemLabelRelations> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProblemLabelRelations::getProblemId, problem.getId());
            queryWrapper.eq(ProblemLabelRelations::getLabelId, labelId);
            int delete = problemLabelRelationsMapper.delete(queryWrapper);
            if (delete < 0) {
                throw new ClientException(ProblemErrorCodeEnum.Problem_UPDATE_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public synchronized void delete(Long id) {
        int result = problemMapper.deleteById(id);
        if (result < 0)
            throw new ClientException(ProblemErrorCodeEnum.Problem_DELETE_ERROR);
        LambdaQueryWrapper<ProblemLabelRelations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProblemLabelRelations::getProblemId, id);
        // 删除其实用的都是逻辑删除
        int delete = problemLabelRelationsMapper.delete(wrapper);
        if (delete < 0)
            throw new ClientException(ProblemErrorCodeEnum.Problem_DELETE_ERROR);
    }

    @Override
    public IPage<ProblemPageRespDTO> pageProblemList(ProblemPageReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        IPage<ProblemPageRespDTO> result = problemMapper.getAll(reqDTO);
        // 计算通过率
        for (ProblemPageRespDTO problemPageRespDTO : result.getRecords()) {
            double passRate = 0.0;
            if (problemPageRespDTO.getSubmitCount() != 0)
                passRate = (double) problemPageRespDTO.getAcceptCount() / problemPageRespDTO.getSubmitCount() * 100.0;
            String passRateStr = String.format("%.1f", passRate) + "%";
            problemPageRespDTO.setPassRate(passRateStr);
        }
        return result;
    }

    @Override
    public ProblemGetByIdRespDTO getProblemById(Long problemId) {
        ProblemGetByIdRespDTO result = null;
        try {
            result = problemMapper.getProblemById(problemId);
        } catch (Exception e) {
            throw new ClientException(ProblemErrorCodeEnum.Problem_NOT_FOUND);
        }

        List<SampleInfoRespSTO> sampleList = new ArrayList<>();
        result.setSampleList(sampleList);
        LambdaQueryWrapper<Sample> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sample::getProblemId, problemId);
        List<Sample> samples = sampleMapper.selectList(wrapper);
        for (Sample sample : samples) {
            SampleInfoRespSTO sampleInfoRespSTO = new SampleInfoRespSTO();
            sampleInfoRespSTO.setSampleInput(sample.getSampleInput());
            sampleInfoRespSTO.setSampleOutput(sample.getSampleOutput());
            sampleList.add(sampleInfoRespSTO);
        }
        return result;
    }

    @Override
    public ProblemPageRespDTO getProblemSimpleById(Long problemId) {
        ProblemPageRespDTO result = problemMapper.getProblemSimpleById(problemId);
        // 计算通过率
        if (result == null) {
            throw new ClientException(ProblemErrorCodeEnum.Problem_NOT_FOUND);
        }
        double passRate = 0.0;
        if (result.getSubmitCount() != 0)
            passRate = (double) result.getAcceptCount() / result.getSubmitCount() * 100.0;
        String passRateStr = String.format("%.1f", passRate) + "%";
        result.setPassRate(passRateStr);
        
        return result;
    }
//        Page<Problem> problemPage = problemMapper.selectPage(reqDTO, null);
//        // 将problemPage中的records都转换成ProblemPageRespDTO 类型
//        Page<ProblemPageRespDTO> problemPageRespDTOPage = new Page<>();
//        BeanUtils.copyProperties(problemPage, problemPageRespDTOPage);
//        List<Problem> records1 = problemPage.getRecords();
//        List<ProblemPageRespDTO> dataList = new ArrayList<>();
//        for (Problem problem : records1) {
//            ProblemPageRespDTO problemPageRespDTO = new ProblemPageRespDTO();
//            BeanUtils.copyProperties(problem, problemPageRespDTO);
//            problemPageRespDTO.setProblemId(problem.getId());
//            double passRate = 0.0;
//            if (problem.getSubmitCount() != 0)
//                passRate = (double) problem.getAcceptCount() / problem.getSubmitCount() * 100.0;
//            String passRateStr = String.format("%.1f", passRate) + "%";
//            problemPageRespDTO.setPassRate(passRateStr);
//            dataList.add(problemPageRespDTO);
//        }
//        problemPageRespDTOPage.setRecords(dataList);
//        // 获取所有题目id, 用于查询用户是否提交过该题目
//        List<ProblemPageRespDTO> records = problemPageRespDTOPage.getRecords();
//        List<Long> ProblemIdList = records.stream().map(ProblemPageRespDTO::getProblemId).collect(Collectors.toList());
//        System.out.println(ProblemIdList);
//        LambdaQueryWrapper<AcceptStatus> acceptStatusLambdaQueryWrapper = new LambdaQueryWrapper<>();
////        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getUserId, UserHolder.getUserId());
//        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getUserId, "1");
//        acceptStatusLambdaQueryWrapper.in(AcceptStatus::getProblemId, ProblemIdList);
//        List<AcceptStatus> acceptStatusList = acceptStatusMapper.selectList(acceptStatusLambdaQueryWrapper);
//        // 将acceptStatusList中的问题id和题目id对应起来
//        Map<Long, Integer> acceptStatusMap = acceptStatusList.stream().collect(Collectors.toMap(AcceptStatus::getProblemId, AcceptStatus::getStatus));
//
//        for (ProblemPageRespDTO problemPageRespDTO : problemPageRespDTOPage.getRecords()) {
//            Long ProblemId = problemPageRespDTO.getProblemId();
//            // 查询题目标签
//            LambdaQueryWrapper<ProblemLabelRelations> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(ProblemLabelRelations::getProblemId, ProblemId);
//            wrapper.select(ProblemLabelRelations::getLabelId);
//            List<ProblemLabelRelations> problemLabelRelations = problemLabelRelationsMapper.selectList(wrapper);
//            List<Long> labelIds = problemLabelRelations.stream().map(ProblemLabelRelations::getLabelId).collect(Collectors.toList());
//            if (!labelIds.isEmpty()) {
//                LambdaQueryWrapper<Label> labelLambdaQueryWrapper = new LambdaQueryWrapper<>();
//                labelLambdaQueryWrapper.in(Label::getId, labelIds);
//                labelLambdaQueryWrapper.select(Label::getLabelName);
//                List<Label> labels = labelMapper.selectList(labelLambdaQueryWrapper);
//                List<String> labelNames = labels.stream().map(Label::getLabelName).collect(Collectors.toList());
//                problemPageRespDTO.setLabels(labelNames);
//            }
//
//            // 查询题目来源
//            LambdaQueryWrapper<ProblemSourceRelations> problemSourceRelationsLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            problemSourceRelationsLambdaQueryWrapper.eq(ProblemSourceRelations::getProblemId, ProblemId);
//            problemSourceRelationsLambdaQueryWrapper.select(ProblemSourceRelations::getSourceId);
//            List<ProblemSourceRelations> problemSourceRelations = problemSourceRelationsMapper.selectList(problemSourceRelationsLambdaQueryWrapper);
//            List<Long> sourceIds = problemSourceRelations.stream().map(ProblemSourceRelations::getSourceId).collect(Collectors.toList());
//            if (!sourceIds.isEmpty()) {
//                LambdaQueryWrapper<Source> sourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
//                sourceLambdaQueryWrapper.in(Source::getId, sourceIds);
//                List<Source> sources = sourceMapper.selectList(sourceLambdaQueryWrapper);
//                List<String> sourceNames = sources.stream().map(Source::getSourceName).collect(Collectors.toList());
//                problemPageRespDTO.setSources(sourceNames);
//            }
//
//            // 查询是否通过
//            problemPageRespDTO.setStatus(acceptStatusMap.getOrDefault(ProblemId, 0));
//        }
//
//        return problemPageRespDTOPage;
}
