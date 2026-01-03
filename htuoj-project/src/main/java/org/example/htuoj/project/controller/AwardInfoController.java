package org.example.htuoj.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.AwardInfoGetAllReqDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetAllRespDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetByUserIDRespDTO;
import org.example.htuoj.project.service.IAwardInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 * 获奖信息 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-20
 */
@RestController
@RequestMapping("/award-info")
public class AwardInfoController {
    @Autowired
    private IAwardInfoService awardInfoService;

    @PostMapping("getAll")
    public Result<IPage<AwardInfoGetAllRespDTO>> getAll(@RequestBody AwardInfoGetAllReqDTO reqDTO) {
        return Results.success(awardInfoService.getAll(reqDTO));
    }


    @GetMapping("getByUserId")
    public Result<List<AwardInfoGetByUserIDRespDTO>> getByUserId(@RequestParam("studentNumber") String studentNumber) {

        return Results.success(awardInfoService.getByUserId(studentNumber));
    }

    @GetMapping("getTrackList")
    public Result<List<String>> getTrackNameList() {
        return Results.success(awardInfoService.getTrackNameList());
    }

    @GetMapping("getCompetitionList")
    public Result<List<String>> getCompetitionNameList() {
        return Results.success(awardInfoService.getCompetitionNameList());
    }


}
