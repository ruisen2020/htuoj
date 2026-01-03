package org.example.htuoj.project.controller;

import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dao.ContestGetRankListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetRankListRespDTO;
import org.example.htuoj.project.service.IContestAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contestAccept")
public class ContestAcceptController {

    @Autowired
    private IContestAcceptService contestAcceptService;

    @PostMapping("/getRankList")
    public Result<List<ContestGetRankListRespDTO>> getRankList(@RequestBody ContestGetRankListReqDTO reqDTO) {
        return Results.success(contestAcceptService.getRankList(reqDTO));
    }

}
