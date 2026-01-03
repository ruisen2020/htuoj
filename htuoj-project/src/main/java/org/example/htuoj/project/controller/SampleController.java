package org.example.htuoj.project.controller;


import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.SampleGetListByProblemIdReqDTO;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;
import org.example.htuoj.project.service.ISampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 样例表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private ISampleService sampleService;

    @PostMapping("getSampleByProblemId")
    public Result<List<SampleGetListByProblemIdRespDTO>> getSampleByProblemId(@RequestBody SampleGetListByProblemIdReqDTO reqDTO) {
        return Results.success(sampleService.getSampleByProblemId(reqDTO));
    }
}
