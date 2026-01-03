package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.UserPreferences;
import org.example.htuoj.common.dto.resp.UserPreferencesGetRespDTO;

import java.io.IOException;

/**
 * <p>
 * 用户偏好表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-16
 */
public interface IUserPreferencesService extends IService<UserPreferences> {

    UserPreferencesGetRespDTO getUserPreferencesInfo(Long userId);


    void updateDaily() throws IOException;
}
