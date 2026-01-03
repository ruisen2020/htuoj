package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.htuoj.common.dao.Follow;
import org.example.htuoj.common.dto.resp.UserGetAllRespDTO;

import java.util.List;

/**
 * <p>
 * 关注表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-09
 */
public interface FollowMapper extends BaseMapper<Follow> {

    List<UserGetAllRespDTO> getFollowList(@Param("userId") Long userId, @Param("myUserId")Long myUserId);

    List<UserGetAllRespDTO> getFansList(@Param("userId") Long userId, @Param("myUserId")Long myUserId);
}
