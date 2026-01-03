package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Like;
import org.example.htuoj.common.dto.req.LikeAddReqDTO;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
public interface ILikeService extends IService<Like> {

    void addLike(LikeAddReqDTO reqDTO) throws InterruptedException;


}
