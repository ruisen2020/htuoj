package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.Source;
import org.example.htuoj.common.dto.resp.SourceGetAllRespDTO;
import org.example.htuoj.common.mapper.SourceMapper;
import org.example.htuoj.project.service.ISourceService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 题目来源表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Service
public class SourceServiceImpl extends ServiceImpl<SourceMapper, Source> implements ISourceService {
    @Autowired
    private SourceMapper sourceMapper;

    @Override
    public List<SourceGetAllRespDTO> getAll() {
        List<Source> sources = sourceMapper.selectList(null);
        List<SourceGetAllRespDTO> result = new ArrayList<>();
        for (Source source : sources) {
            SourceGetAllRespDTO sourceGetAllRespDTO = new SourceGetAllRespDTO();
            sourceGetAllRespDTO.setValue(String.valueOf(source.getId()));
            sourceGetAllRespDTO.setLabel(source.getSourceName());
            result.add(sourceGetAllRespDTO);
        }
        return result;
    }
}
