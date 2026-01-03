package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Collect;
import org.example.htuoj.common.dto.req.CollectAddReqDTO;
import org.example.htuoj.common.dto.req.CollectGetCollectListByUserIdReqDTO;
import org.example.htuoj.common.dto.resp.CollectGetCollectListByUseIdRespDTO;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
public interface ICollectService extends IService<Collect> {

    void addCollect(CollectAddReqDTO reqDTO);

    IPage<CollectGetCollectListByUseIdRespDTO> getCollectListByUseId(CollectGetCollectListByUserIdReqDTO reqDTO);
}
