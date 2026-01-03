package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dto.req.SampleGetListByProblemIdReqDTO;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.Sample;
import org.example.htuoj.common.mapper.SampleMapper;
import org.example.htuoj.project.service.ISampleService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 样例表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
@Service
public class SampleServiceImpl extends ServiceImpl<SampleMapper, Sample> implements ISampleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Override
    public List<SampleGetListByProblemIdRespDTO> getSampleByProblemId(SampleGetListByProblemIdReqDTO reqDTO) {
        LambdaQueryWrapper<Sample> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sample::getProblemId, reqDTO.getProblemId());
        List<Sample> samples = sampleMapper.selectList(wrapper);
        List<SampleGetListByProblemIdRespDTO> respDTOS = new ArrayList<>();
        for (Sample sample : samples) {
            SampleGetListByProblemIdRespDTO sampleGetListByProblemIdRespDTO = new SampleGetListByProblemIdRespDTO();
            sampleGetListByProblemIdRespDTO.setInput(sample.getSampleInput());
            sampleGetListByProblemIdRespDTO.setOutput(sample.getSampleOutput());
            respDTOS.add(sampleGetListByProblemIdRespDTO);
        }
        return respDTOS;
    }
}
