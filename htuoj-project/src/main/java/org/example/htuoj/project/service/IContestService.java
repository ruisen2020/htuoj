package org.example.htuoj.project.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Contest;
import org.example.htuoj.common.dto.req.ContestGetByIdReqDTO;
import org.example.htuoj.common.dto.req.ContestGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetContestListByOtherOJRespDTO;
import org.example.htuoj.common.dto.resp.ContestGetListRespDTO;

import java.util.List;

/**
 * <p>
 * 竞赛表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-11-28
 */
public interface IContestService extends IService<Contest> {

    List<ContestGetContestListByOtherOJRespDTO> getContestListByOtherOJ();

    IPage<ContestGetListRespDTO> getContestList(ContestGetListReqDTO reqDTO);

    ContestGetListRespDTO getContestById(ContestGetByIdReqDTO reqDTO);
}
