package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.AwardInfo;
import org.example.htuoj.common.dto.req.AwardInfoGetAllReqDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetAllRespDTO;


import java.util.List;

/**
 * <p>
 * 获奖信息 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-20
 */
public interface AwardInfoMapper extends BaseMapper<AwardInfo> {
//    List<AwardInfoGetAllRespDTO> getAll(AwardInfoGetAllRespDTO reqDTO);
    List<AwardInfoGetAllRespDTO> getAllList(AwardInfoGetAllReqDTO reqDTO);

    IPage<AwardInfoGetAllRespDTO> getAllList2(AwardInfoGetAllReqDTO reqDTO);

    List<String> getTrackNameList();

    List<String> getCompetitionNameList();
}
