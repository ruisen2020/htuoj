package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.resp.UserGetUserInfoRespDTO;
import org.example.htuoj.common.dto.resp.UserGetUserTopListRespDTO;
import org.example.htuoj.common.dto.resp.UserLoginRespDTO;
import org.example.htuoj.project.service.IUserService;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/sendHeartbeat")
    public Result<Void> hello() {
        return Results.success(userService.sendHeartbeat());
    }

    @GetMapping("/checkLogin")
    public Result<Boolean> checkLogin() {
        return Results.success(StpUtil.isLogin());
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody UserAddReqDTO reqDTO) {
        userService.register(reqDTO);
        return Results.success();
    }

    /**
     * 登录
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO reqDTO) {
        return Results.success(userService.login(reqDTO));
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        userService.logout();
        return Results.success();
    }

    @PostMapping("updateAvatar")
    @SaCheckLogin
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file) {

        return Results.success(userService.updateAvatar(file));
    }

    /**
     * 获取头像
     *
     * @return
     */
    @GetMapping("getAvatar")
    public Result<String> getAvatar() {
        return Results.success(userService.getAvatar());
    }

    /**
     * 分页获取所有用户信息
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("getAll")
    public Result<IPage<UserGetUserInfoRespDTO>> getAll(@RequestBody UserGetAllReqDTO reqDTO) {
        return Results.success(userService.getAll(reqDTO));
    }

    @PostMapping("getUserTopList")
    public Result<IPage<UserGetUserTopListRespDTO>> getUserTopList(@RequestBody UserGetUserTopListReqDTO reqDTO) {
        return Results.success(userService.getUserTopList(reqDTO));
    }

    @GetMapping("getUserInfo")
    public Result<UserGetUserInfoRespDTO> getUserInfo(@RequestParam("userId") Long userId) {
        return Results.success(userService.getUserInfoById(userId));
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("getUserInfoById")
    public Result<UserGetUserInfoRespDTO> getUserInfoById(Long id) {
        return Results.success(userService.getUserInfoById(id));
    }

    /**
     * 获取验证码
     *
     * @param number
     * @return
     */
    @GetMapping("getCaptcha")
    public Result<Void> getCaptcha(@RequestParam("number") String number) {
        return Results.success(userService.getCaptcha(number));
    }

    @PostMapping("getCaptchaRegister")
    public Result<Void> getCaptchaRegister(@RequestBody UserGetCaptchaRegisterReqDTO reqDTO) {
        return Results.success(userService.getCaptchaRegister(reqDTO));
    }

    @PostMapping("resetPassword")
    public Result<Void> resetPassword(@RequestBody UserResetPasswordReqDTO reqDTO) {
        userService.resetPassword(reqDTO);
        return Results.success();
    }

    @PostMapping("updateUserAllInfo")
    @SaCheckLogin
    public Result<Void> updateUserAllInfo(@RequestBody UserUpdateAllInfoReqDTO reqDTO) {
        userService.updateUserAllInfo(reqDTO);
        return Results.success();
    }


}
