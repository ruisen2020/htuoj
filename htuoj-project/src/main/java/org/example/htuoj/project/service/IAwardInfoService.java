package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.AwardInfo;
import org.example.htuoj.common.dto.req.AwardInfoGetAllReqDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetAllRespDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetByUserIDRespDTO;

import java.util.List;

/**
 * <p>
 * 获奖信息 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-20
 */
public interface IAwardInfoService extends IService<AwardInfo> {

    IPage<AwardInfoGetAllRespDTO> getAll(AwardInfoGetAllReqDTO reqDTO);

    List<AwardInfoGetByUserIDRespDTO> getByUserId(String studentNumber);


    List<String> getTrackNameList();

    List<String> getCompetitionNameList();
}
