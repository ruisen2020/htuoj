package org.example.htuoj.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.Notice;
import org.example.htuoj.common.dto.req.NoticeClearReqDTO;
import org.example.htuoj.common.dto.req.NoticeGetNoticeListReqDTO;
import org.example.htuoj.common.dto.resp.NoticeGetNoticeListRespDTO;
import org.example.htuoj.common.mapper.NoticeMapper;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.service.INoticeService;
import org.python.antlr.op.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {


    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public IPage<NoticeGetNoticeListRespDTO> getNoticeList(NoticeGetNoticeListReqDTO reqDTO) {
        reqDTO.setReceiverId(UserHolder.getUserId());
        System.out.println(reqDTO.getIsRead());
        IPage<NoticeGetNoticeListRespDTO> respDTO = noticeMapper.getNoticeList(reqDTO);
        return respDTO;
    }

    @Override
    public Void clearRead(NoticeClearReqDTO reqDTO) {
        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        if (reqDTO.getTargetTypeList() != null && !reqDTO.getTargetTypeList().isEmpty())
            updateWrapper.in(Notice::getTargetType, reqDTO.getTargetTypeList());
        updateWrapper.set(Notice::getIsRead, true);
        noticeMapper.update(null, updateWrapper);
        return null;
    }

    @Override
    public List<Long> getRedPoint() {
        ArrayList<Long> res = new ArrayList<>();
        if (UserHolder.getUserId() == null) {
            res = new ArrayList<>(5);
            return res;
        }
        LambdaQueryWrapper<Notice> noticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        noticeLambdaQueryWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        noticeLambdaQueryWrapper.eq(Notice::getIsRead, false);
        // 全部消息
        Long l1 = noticeMapper.selectCount(noticeLambdaQueryWrapper);
        res.add(l1);
        // 关注消息
        noticeLambdaQueryWrapper.clear();
        noticeLambdaQueryWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        noticeLambdaQueryWrapper.eq(Notice::getTargetType, 4);
        noticeLambdaQueryWrapper.eq(Notice::getIsRead, false);
        Long l2 = noticeMapper.selectCount(noticeLambdaQueryWrapper);
        res.add(l2);
        // 点赞消息
        noticeLambdaQueryWrapper.clear();
        noticeLambdaQueryWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        noticeLambdaQueryWrapper.in(Notice::getTargetType, 0, 1);
        noticeLambdaQueryWrapper.eq(Notice::getIsRead, false);
        Long l4 = noticeMapper.selectCount(noticeLambdaQueryWrapper);
        res.add(l4);
        // 收藏消息
        noticeLambdaQueryWrapper.clear();
        noticeLambdaQueryWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        noticeLambdaQueryWrapper.eq(Notice::getTargetType, 5);
        noticeLambdaQueryWrapper.eq(Notice::getIsRead, false);
        Long l5 = noticeMapper.selectCount(noticeLambdaQueryWrapper);
        res.add(l5);
        // 评论消息
        noticeLambdaQueryWrapper.clear();
        noticeLambdaQueryWrapper.eq(Notice::getReceiverId, UserHolder.getUserId());
        noticeLambdaQueryWrapper.in(Notice::getTargetType, 2, 3);
        noticeLambdaQueryWrapper.eq(Notice::getIsRead, false);
        Long l3 = noticeMapper.selectCount(noticeLambdaQueryWrapper);
        res.add(l3);
        return res;
    }


}
