package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dto.req.ProblemAddReqDTO;
import org.example.htuoj.common.dto.req.ProblemPageReqDTO;
import org.example.htuoj.common.dto.req.ProblemUpdateReqDTO;
import org.example.htuoj.common.dto.resp.ProblemGetByIdRespDTO;
import org.example.htuoj.common.dto.resp.ProblemPageRespDTO;

/**
 * <p>
 * 题目表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
public interface IProblemService extends IService<Problem> {
    Result<Void>  add(ProblemAddReqDTO reqDTO);

    void update(ProblemUpdateReqDTO reqDTO);

    void delete(Long id);


    IPage<ProblemPageRespDTO> pageProblemList(ProblemPageReqDTO reqDTO);


    ProblemGetByIdRespDTO getProblemById(Long problemId);

    ProblemPageRespDTO getProblemSimpleById(Long problemId);
}
