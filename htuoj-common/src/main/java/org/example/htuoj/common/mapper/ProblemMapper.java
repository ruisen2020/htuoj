package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dto.req.ProblemPageReqDTO;
import org.example.htuoj.common.dto.resp.ProblemGetByIdRespDTO;
import org.example.htuoj.common.dto.resp.ProblemPageRespDTO;

/**
 * <p>
 * 题目表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
public interface ProblemMapper extends BaseMapper<Problem> {

    // 写一个插入元素的方法
    int add(Problem problem);

    IPage<ProblemPageRespDTO> getAll(ProblemPageReqDTO reqDTO);

    ProblemGetByIdRespDTO getProblemById(Long problemId);

    ProblemPageRespDTO getProblemSimpleById(Long problemId);


//    Long getCount(ProblemPageReqCopyDTO reqDTO);
//
//    List<ProblemPageRespDTO> getLabelsAndSources(ProblemGetLabelsAndSourcesReqDTO reqDTO);
}
