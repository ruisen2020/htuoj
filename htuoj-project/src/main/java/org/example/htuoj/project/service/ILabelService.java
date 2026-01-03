package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Label;
import org.example.htuoj.common.dto.req.LabelAddReqDTO;
import org.example.htuoj.common.dto.resp.LabelGetAllRespDTO;

import java.util.List;

/**
 * <p>
 * 算法标签 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-16
 */
public interface ILabelService extends IService<Label> {


    void add(LabelAddReqDTO reqDTO);

    List<LabelGetAllRespDTO> getAll();
}
