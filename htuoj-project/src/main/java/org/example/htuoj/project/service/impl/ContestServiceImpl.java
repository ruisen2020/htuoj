package org.example.htuoj.project.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.htuoj.common.dao.Contest;
import org.example.htuoj.common.dto.req.ContestGetByIdReqDTO;
import org.example.htuoj.common.dto.req.ContestGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetContestListByOtherOJRespDTO;
import org.example.htuoj.common.dto.resp.ContestGetListRespDTO;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.crawler.ContestCrawler;
import org.example.htuoj.common.mapper.ContestMapper;
import org.example.htuoj.project.service.IContestService;
import org.example.htuoj.project.utils.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.project.utils.RedisConstant.CONTEST_LIST_KEY;

/**
 * <p>
 * 竞赛表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-11-28
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements IContestService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContestCrawler contestCrawler;

    @Override
    public List<ContestGetContestListByOtherOJRespDTO> getContestListByOtherOJ() {
        List<ContestGetContestListByOtherOJRespDTO> result = new ArrayList<>();
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(CONTEST_LIST_KEY);
        ObjectMapper objectMapper = new ObjectMapper();
        if (!entries.isEmpty()) {
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                try {
                    ContestGetContestListByOtherOJRespDTO userGetUserTopListRespDTO = objectMapper.readValue((String) entry.getValue(), ContestGetContestListByOtherOJRespDTO.class);
                    result.add(userGetUserTopListRespDTO);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // 查询
            result = contestCrawler.AllContest();
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    ContestGetContestListByOtherOJRespDTO respDTO = result.get(i);
                    try {
                        stringRedisTemplate.opsForHash().put(RedisConstant.CONTEST_LIST_KEY, String.valueOf(i + 1), objectMapper.writeValueAsString(respDTO));
                        stringRedisTemplate.expire(RedisConstant.CONTEST_LIST_KEY, 1, TimeUnit.DAYS);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        Collections.sort(result, new Comparator<ContestGetContestListByOtherOJRespDTO>() {
            @Override
            public int compare(ContestGetContestListByOtherOJRespDTO o1, ContestGetContestListByOtherOJRespDTO o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        return result;
    }

    @Override
    public IPage<ContestGetListRespDTO> getContestList(ContestGetListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        return baseMapper.getContestList(reqDTO);
    }

    @Override
    public ContestGetListRespDTO getContestById(ContestGetByIdReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        return baseMapper.getContestById(reqDTO);
    }


}
