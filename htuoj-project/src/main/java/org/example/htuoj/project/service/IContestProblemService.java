package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.ContestProblem;
import org.example.htuoj.common.dto.req.ContestProblemGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetListRespDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetMapRespDTO;

import java.util.List;

public interface IContestProblemService  extends IService<ContestProblem> {
    List<ContestProblemGetListRespDTO> getContestProblemList(ContestProblemGetListReqDTO reqDTO);

    List<ContestProblemGetMapRespDTO> getContestProblemMap(Long contestId);
}
