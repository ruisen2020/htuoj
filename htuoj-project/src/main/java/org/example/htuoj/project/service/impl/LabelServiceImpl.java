package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Label;
import org.example.htuoj.common.dto.req.LabelAddReqDTO;
import org.example.htuoj.common.dto.resp.LabelGetAllRespDTO;
import org.example.htuoj.common.mapper.LabelMapper;
import org.example.htuoj.project.service.ILabelService;

import java.util.ArrayList;
import java.util.List;

import static org.example.htuoj.project.enums.LabelErrorCodeEnum.LABEL_SAVE_ERROR;

/**
 * <p>
 * 算法标签 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-16
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements ILabelService {

    @Autowired
    private LabelMapper labelMapper;

    @Override
    public void add(LabelAddReqDTO reqDTO) {
        Label label = new Label();
        BeanUtils.copyProperties(reqDTO, label);
        int result = labelMapper.insert(label);
        if (result < 1)
            throw new ClientException(LABEL_SAVE_ERROR);
    }

    @Override
    public List<LabelGetAllRespDTO> getAll() {
        List<Label> labels = labelMapper.selectList(null);
        List<LabelGetAllRespDTO> result = new ArrayList<>();
        for (Label label : labels) {
            LabelGetAllRespDTO labelGetAllRespDTO = new LabelGetAllRespDTO();
            labelGetAllRespDTO.setValue(String.valueOf(label.getId()));
            labelGetAllRespDTO.setLabel(label.getLabelName());
            result.add(labelGetAllRespDTO);
        }
        return result;
    }


}
