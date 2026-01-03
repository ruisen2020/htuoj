package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Source;
import org.example.htuoj.common.dto.resp.SourceGetAllRespDTO;

import java.util.List;

/**
 * <p>
 * 题目来源表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
public interface ISourceService extends IService<Source> {

    List<SourceGetAllRespDTO> getAll();
}
