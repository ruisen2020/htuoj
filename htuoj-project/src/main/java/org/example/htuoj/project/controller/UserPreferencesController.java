package org.example.htuoj.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.resp.UserPreferencesGetRespDTO;
import org.example.htuoj.project.service.IUserPreferencesService;

import java.io.IOException;

/**
 * <p>
 * 用户偏好表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-16
 */
@RestController
@RequestMapping("/user-preferences")
public class UserPreferencesController {

    @Autowired
    private IUserPreferencesService userPreferencesService;

    @GetMapping("getUserPreferencesInfo")
    public Result<UserPreferencesGetRespDTO> getUserPreferencesInfo(@RequestParam("userId") Long userId) {

        return Results.success(userPreferencesService.getUserPreferencesInfo(userId));
    }

    /**
     * 每日更新用户在其他OJ的过题数和rating
     *
     * @return
     * @throws IOException
     */
    @GetMapping("updateDaily")
    public Result<Void> updateDaily() throws IOException {
        userPreferencesService.updateDaily();
        return Results.success();
    }

}
