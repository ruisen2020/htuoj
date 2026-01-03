package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Notice;
import org.example.htuoj.common.dao.Source;
import org.example.htuoj.common.dto.req.NoticeGetNoticeListReqDTO;
import org.example.htuoj.common.dto.resp.NoticeGetNoticeListRespDTO;

/**
 * <p>
 * 题目来源表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    IPage<NoticeGetNoticeListRespDTO> getNoticeList(NoticeGetNoticeListReqDTO reqDTO);
}
