package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Collect;
import org.example.htuoj.common.dto.req.CollectGetCollectListByUserIdReqDTO;
import org.example.htuoj.common.dto.resp.CollectGetCollectListByUseIdRespDTO;


/**
 * <p>
 * 点赞表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
public interface CollectMapper extends BaseMapper<Collect> {

    IPage<CollectGetCollectListByUseIdRespDTO> getCollectListByUseId(CollectGetCollectListByUserIdReqDTO reqDTO);
}
