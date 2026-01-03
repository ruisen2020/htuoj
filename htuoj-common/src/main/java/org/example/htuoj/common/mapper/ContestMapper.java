package org.example.htuoj.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Contest;
import org.example.htuoj.common.dto.req.ContestGetByIdReqDTO;
import org.example.htuoj.common.dto.req.ContestGetListReqDTO;
import org.example.htuoj.common.dto.resp.ContestGetListRespDTO;

/**
 * <p>
 * 竞赛表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-11-28
 */
public interface ContestMapper extends BaseMapper<Contest> {

    IPage<ContestGetListRespDTO> getContestList(ContestGetListReqDTO reqDTO);

    ContestGetListRespDTO getContestById(ContestGetByIdReqDTO reqDTO);
}
