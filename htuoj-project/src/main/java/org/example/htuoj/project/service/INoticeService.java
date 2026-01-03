package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.NoticeClearReqDTO;
import org.example.htuoj.common.dto.req.NoticeGetNoticeListReqDTO;
import org.example.htuoj.common.dto.resp.NoticeGetNoticeListRespDTO;

import java.util.List;

public interface INoticeService {
    IPage<NoticeGetNoticeListRespDTO> getNoticeList(NoticeGetNoticeListReqDTO reqDTO);

    Void clearRead(NoticeClearReqDTO reqDTO);

    List<Long> getRedPoint();
}
