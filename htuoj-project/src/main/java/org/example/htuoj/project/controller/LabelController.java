package org.example.htuoj.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.LabelAddReqDTO;
import org.example.htuoj.common.dto.resp.LabelGetAllRespDTO;
import org.example.htuoj.project.service.ILabelService;

import java.util.List;

/**
 * <p>
 * 算法标签 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-16
 */
@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private ILabelService labelService;

    @PostMapping("/add")
    public Result<Void> add(LabelAddReqDTO reqDTO) {
        labelService.add(reqDTO);
        return Results.success();
    }

    @GetMapping("/getAll")
    public Result<List<LabelGetAllRespDTO>> getAll() {
        return Results.success(labelService.getAll());
    }

//    @PostMapping("/delete")
//    public Result<Void> delete(Long id) {
//        labelService.removeById(id);
//        return Results.success();
//    }
}
