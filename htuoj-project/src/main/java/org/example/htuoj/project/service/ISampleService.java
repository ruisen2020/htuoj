package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Sample;
import org.example.htuoj.common.dto.req.SampleGetListByProblemIdReqDTO;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;

import java.util.List;

/**
 * <p>
 * 样例表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
public interface ISampleService extends IService<Sample> {

    List<SampleGetListByProblemIdRespDTO> getSampleByProblemId(SampleGetListByProblemIdReqDTO reqDTO);
}
