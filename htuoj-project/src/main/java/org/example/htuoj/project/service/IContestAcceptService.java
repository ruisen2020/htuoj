package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.ContestAccept;
import org.example.htuoj.common.dao.ContestGetRankListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetRankListRespDTO;

import java.util.List;

public interface IContestAcceptService extends IService<ContestAccept> {
    List< ContestGetRankListRespDTO> getRankList(ContestGetRankListReqDTO reqDTO);
}
