package org.example.htuoj.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.ContestRegisterAddReqDTO;
import org.example.htuoj.project.service.IContestRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contestRegister")
public class ContestRegisterController {
    @Autowired
    private IContestRegisterService  contestRegisterService;

    @PostMapping("addRegister")
    @SaCheckLogin
    public Result<Void> addRegister(@RequestBody ContestRegisterAddReqDTO reqDTO)
    {
        contestRegisterService.addRegister(reqDTO);
        return Results.success();
    }

    @GetMapping("/checkRegister/{contestId}")
    @SaCheckLogin
    public Result<Void> checkRegister(@PathVariable("contestId") Long contestId)
    {
        contestRegisterService.checkRegister(contestId);
        return Results.success();
    }
}
