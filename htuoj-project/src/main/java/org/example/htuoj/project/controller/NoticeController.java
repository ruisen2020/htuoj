package org.example.htuoj.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.NoticeClearReqDTO;
import org.example.htuoj.common.dto.req.NoticeGetNoticeListReqDTO;
import org.example.htuoj.common.dto.resp.NoticeGetNoticeListRespDTO;
import org.example.htuoj.project.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @PostMapping("/getNoticeList")
    public Result<IPage<NoticeGetNoticeListRespDTO>> getNoticeList(@RequestBody NoticeGetNoticeListReqDTO reqDTO) {
        return Results.success(noticeService.getNoticeList(reqDTO));
    }

    @PostMapping("/clearRead")
    public Result<Void> clearRead(@RequestBody NoticeClearReqDTO reqDTO) {
        return Results.success(noticeService.clearRead(reqDTO));
    }

    @PostMapping("/getRedPoint")
    public Result<List<Long>> getRedPoint() {
        return Results.success(noticeService.getRedPoint());
    }

}
