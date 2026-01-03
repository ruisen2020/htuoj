package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.FollowFollowOtherRespDTO;
import org.example.htuoj.common.dto.resp.FollowGetCountRespDTO;
import org.example.htuoj.common.dto.resp.FollowGetFollowInfo;
import org.example.htuoj.project.service.IFollowService;

/**
 * <p>
 * 关注表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-09
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private IFollowService followService;

    @GetMapping("/getFollowCount")
    public Result<FollowGetCountRespDTO> followCount(Long id) {
        return Results.success(followService.getCount(id));
    }

    /**
     * 获取关注信息(关注列表、粉丝列表)
     *
     * @param userId
     * @return
     */
    @GetMapping("/getFollowInfo")
    public Result<FollowGetFollowInfo> getFollowInfo(Long userId) {
        return Results.success(followService.getFollowInfo(userId));
    }

    @PostMapping("/followOther")
    @SaCheckLogin
    public Result<Void> followOther(@RequestBody FollowFollowOtherRespDTO respDTO) {
        followService.followOther(respDTO.getUserId());
        return Results.success();
    }

//    @PostMapping("/cancelFollow")
//    public Result<Void> cancelFollow(@RequestBody FollowCancelReqDTO respDTO) {
//        followService.cancelFollow(respDTO);
//        return Results.success();
//    }
}
