package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.htuoj.common.dao.ContestAccept;
import org.example.htuoj.common.dao.ContestGetRankListReqDTO;
import org.example.htuoj.common.dao.PassProblemDetail;
import org.example.htuoj.common.dto.resp.ContestGetRankListRespDTO;

import java.util.List;

public interface ContestAcceptMapper extends BaseMapper<ContestAccept> {
    List<ContestGetRankListRespDTO> getRankList(ContestGetRankListReqDTO reqDTO);


    List<PassProblemDetail> getPassList(ContestGetRankListReqDTO reqDTO);
}
