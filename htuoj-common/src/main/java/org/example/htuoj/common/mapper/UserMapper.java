package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.User;
import org.example.htuoj.common.dto.req.UserGetAllReqDTO;
import org.example.htuoj.common.dto.req.UserGetUserTopListReqDTO;
import org.example.htuoj.common.dto.resp.UserGetUserInfoRespDTO;
import org.example.htuoj.common.dto.resp.UserGetUserTopListRespDTO;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
public interface UserMapper extends BaseMapper<User> {
   IPage<UserGetUserInfoRespDTO> getAll(UserGetAllReqDTO reqDTO);

   IPage<UserGetUserTopListRespDTO> getUserTopList(UserGetUserTopListReqDTO reqDTO);
}
