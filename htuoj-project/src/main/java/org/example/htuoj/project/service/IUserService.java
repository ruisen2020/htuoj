package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dto.req.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.htuoj.common.dao.User;
import org.example.htuoj.common.dto.resp.UserGetUserInfoRespDTO;
import org.example.htuoj.common.dto.resp.UserGetUserTopListRespDTO;
import org.example.htuoj.common.dto.resp.UserLoginRespDTO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
public interface IUserService extends IService<User> {
    Long getUserIdByNumber(String number);

    void register(UserAddReqDTO reqDTO);

    UserLoginRespDTO login(UserLoginReqDTO reqDTO);

    String updateAvatar(MultipartFile avatar);

    IPage<UserGetUserInfoRespDTO> getAll(UserGetAllReqDTO reqDTO);

    String getAvatar();

    UserGetUserInfoRespDTO getUserInfoById(Long id);

    Void getCaptcha(String number);

    void resetPassword(UserResetPasswordReqDTO reqDTO);

    Void getCaptchaRegister(UserGetCaptchaRegisterReqDTO number);

    void updateUserAllInfo(UserUpdateAllInfoReqDTO reqDTO);

    IPage<UserGetUserTopListRespDTO> getUserTopList(UserGetUserTopListReqDTO reqDTO);

    void logout();

    Void sendHeartbeat();
}
