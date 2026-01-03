package org.example.htuoj.project.controller;

import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.ContestProblemGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetListRespDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetMapRespDTO;
import org.example.htuoj.project.service.IContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contestProblem")
public class ContestProblemController {

    @Autowired
    private IContestProblemService contestProblemService;

    @PostMapping("/getContestProblemList")
    public Result<List<ContestProblemGetListRespDTO>> getContestProblemList(@RequestBody ContestProblemGetListReqDTO reqDTO) {
        return Results.success(contestProblemService.getContestProblemList(reqDTO));
    }

    @GetMapping("/getContestProblemMap")
    public Result<List<ContestProblemGetMapRespDTO>> getContestProblemMap(@RequestParam("contestId") Long contestId) {
        return Results.success(contestProblemService.getContestProblemMap(contestId));
    }
}
