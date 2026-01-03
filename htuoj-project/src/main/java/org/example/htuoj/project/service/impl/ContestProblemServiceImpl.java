package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.ContestProblem;
import org.example.htuoj.common.dto.req.ContestProblemGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetListRespDTO;
import org.example.htuoj.common.dto.resp.ContestProblemGetMapRespDTO;
import org.example.htuoj.common.mapper.ContestProblemMapper;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.service.IContestProblemService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblem> implements IContestProblemService {
    @Override
    public List<ContestProblemGetListRespDTO> getContestProblemList(ContestProblemGetListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        return baseMapper.getContestProblemList(reqDTO);
    }

    @Override
    public List<ContestProblemGetMapRespDTO> getContestProblemMap(Long contestId) {
        LambdaQueryWrapper<ContestProblem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContestProblem::getContestId, contestId);
        wrapper.eq(ContestProblem::getDelFlag, 0);
        wrapper.select(ContestProblem::getDisplayId, ContestProblem::getProblemId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(wrapper);
        ArrayList<ContestProblemGetMapRespDTO> result = new ArrayList<>();
        for (Map<String, Object> map : maps)
        {
            ContestProblemGetMapRespDTO dto = new ContestProblemGetMapRespDTO();
            dto.setText((String) map.get("display_id"));
            dto.setValue((Long) map.get("problem_id"));
            result.add(dto);
        }
        result.sort(Comparator.comparing(ContestProblemGetMapRespDTO::getText));
        return result;

    }
}
