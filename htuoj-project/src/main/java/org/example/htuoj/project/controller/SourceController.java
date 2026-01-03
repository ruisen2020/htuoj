package org.example.htuoj.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.resp.SourceGetAllRespDTO;
import org.example.htuoj.project.service.ISourceService;

import java.util.List;

/**
 * <p>
 * 题目来源表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@RestController
@RequestMapping("/source")
public class SourceController {
    @Autowired
    private ISourceService sourceService;

    @GetMapping("/getAll")
    public Result<List<SourceGetAllRespDTO>> getAll() {
        return Results.success(sourceService.getAll());
    }
}
