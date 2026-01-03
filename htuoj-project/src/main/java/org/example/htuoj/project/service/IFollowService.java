package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Follow;
import org.example.htuoj.common.dto.resp.FollowGetCountRespDTO;
import org.example.htuoj.common.dto.resp.FollowGetFollowInfo;

/**
 * <p>
 * 关注表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-09
 */
public interface IFollowService extends IService<Follow> {

    FollowGetCountRespDTO getCount(Long id);

    FollowGetFollowInfo getFollowInfo(Long userId);

    void followOther(Long userId);

//    void cancelFollow(FollowCancelReqDTO respDTO);
}
