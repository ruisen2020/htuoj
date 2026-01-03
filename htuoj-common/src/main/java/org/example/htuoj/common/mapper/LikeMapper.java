package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.htuoj.common.dao.Like;
import org.example.htuoj.common.dto.req.LikeAddReqDTO;

/**
 * <p>
 * 点赞表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
public interface LikeMapper extends BaseMapper<Like> {

    int addLike(LikeAddReqDTO reqDTO);
}
