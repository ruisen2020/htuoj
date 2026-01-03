package org.example.htuoj.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.ProblemAddReqDTO;
import org.example.htuoj.common.dto.req.ProblemPageReqDTO;
import org.example.htuoj.common.dto.req.ProblemUpdateReqDTO;
import org.example.htuoj.common.dto.resp.ProblemGetByIdRespDTO;
import org.example.htuoj.common.dto.resp.ProblemPageRespDTO;
import org.example.htuoj.project.service.IProblemService;

/**
 * <p>
 * 题目表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private IProblemService problemService;

    /**
     * 添加题目
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody ProblemAddReqDTO reqDTO) {
        return problemService.add(reqDTO);
    }

    /**
     * @param reqDTO
     * @return
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ProblemUpdateReqDTO reqDTO) {
        problemService.update(reqDTO);
        return Results.success();
    }

    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        problemService.delete(id);
        return Results.success();
    }

    /**
     * 分页查询题目列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/getProblemList")
    public Result<IPage<ProblemPageRespDTO>> pageProblemList(@RequestBody ProblemPageReqDTO reqDTO) {
        return Results.success(problemService.pageProblemList(reqDTO));
    }

    @GetMapping("/getProblemById")
    public Result<ProblemGetByIdRespDTO> getProblemById(@RequestParam("problemId") Long problemId) {

        return Results.success(problemService.getProblemById(problemId));
    }


    @GetMapping("/getProblemSimpleById")
    public Result<ProblemPageRespDTO> getProblemSimpleById(@RequestParam("problemId") Long problemId) {

        return Results.success(problemService.getProblemSimpleById(problemId));
    }


}
