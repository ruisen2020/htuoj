package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.htuoj.common.dao.ContestProblem;
import org.example.htuoj.common.dto.req.ContestProblemGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetListRespDTO;

import java.util.List;

public interface ContestProblemMapper extends BaseMapper<ContestProblem> {
    List<ContestProblemGetListRespDTO> getContestProblemList(ContestProblemGetListReqDTO reqDTO);
}
