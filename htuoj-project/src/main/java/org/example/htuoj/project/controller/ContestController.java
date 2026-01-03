package org.example.htuoj.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.ContestGetByIdReqDTO;
import org.example.htuoj.common.dto.req.ContestGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetContestListByOtherOJRespDTO;
import org.example.htuoj.common.dto.resp.ContestGetListRespDTO;
import org.example.htuoj.project.service.IContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 竞赛表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-11-28
 */
@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    private IContestService contestService;


    @GetMapping("/getContestListByOtherOJ")
    public Result<List<ContestGetContestListByOtherOJRespDTO>> getContestListByOtherOJ() {
        return Results.success(null);
//        return Results.success(contestService.getContestListByOtherOJ());
    }

    @PostMapping("/getContestList")
    public Result<IPage<ContestGetListRespDTO>> getContestList(@RequestBody ContestGetListReqDTO reqDTO) {
        return Results.success(contestService.getContestList(reqDTO));
    }

    @PostMapping("/getContestById")
    public Result<ContestGetListRespDTO> getContestById(@RequestBody ContestGetByIdReqDTO reqDTO) {
        return Results.success(contestService.getContestById(reqDTO));
    }
}
