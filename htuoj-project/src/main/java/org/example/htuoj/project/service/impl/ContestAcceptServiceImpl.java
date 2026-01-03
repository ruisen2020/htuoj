package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.ContestAccept;
import org.example.htuoj.common.dao.ContestGetRankListReqDTO;
import org.example.htuoj.common.dao.PassProblemDetail;
import org.example.htuoj.common.dto.resp.ContestGetRankListRespDTO;
import org.example.htuoj.common.mapper.ContestAcceptMapper;
import org.example.htuoj.project.service.IContestAcceptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ContestAcceptServiceImpl extends ServiceImpl<ContestAcceptMapper, ContestAccept> implements IContestAcceptService {
    @Override
    public List<ContestGetRankListRespDTO> getRankList(ContestGetRankListReqDTO reqDTO) {
        List<ContestGetRankListRespDTO> result = baseMapper.getRankList(reqDTO);
        result.sort(new Comparator<ContestGetRankListRespDTO>() {
            @Override
            public int compare(ContestGetRankListRespDTO o1, ContestGetRankListRespDTO o2) {
                if (!Objects.equals(o1.getAcceptSum(), o2.getAcceptSum())) {
                    return o2.getAcceptSum() - o1.getAcceptSum();
                }
                return o1.getPenaltyTimeSum() - o2.getPenaltyTimeSum();
            }

        });
        int rank = 1;

        for (int i = 0; i < result.size(); i++) {
            if (i == 0) {
                result.get(0).setRank(1);
            } else {
                if (!Objects.equals(result.get(i).getAcceptSum(), result.get(i - 1).getAcceptSum()) || !Objects.equals(result.get(i).getPenaltyTimeSum(), result.get(i - 1).getPenaltyTimeSum())) {
                    rank = i + 1;
                }
                result.get(i).setRank(rank);
            }
            reqDTO.setUserId(result.get(i).getUserId());
            List<PassProblemDetail> passList = baseMapper.getPassList(reqDTO);

            passList.sort(new Comparator<PassProblemDetail>() {
                @Override
                public int compare(PassProblemDetail o1, PassProblemDetail o2) {
                    return o1.getDisplayId().compareTo(o2.getDisplayId());
                }
            });
            result.get(i).setPassList(passList);
        }
        return result;
    }
}
