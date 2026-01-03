package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dto.req.CollectGetCollectListByUserIdReqDTO;
import org.example.htuoj.common.dto.resp.CollectGetCollectListByUseIdRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.CollectAddReqDTO;
import org.example.htuoj.project.service.ICollectService;

/**
 * <p>
 * 点赞表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private ICollectService collectService;


    @RequestMapping("/addCollect")
    @SaCheckLogin
    public Result<Void> addCollect(@RequestBody CollectAddReqDTO reqDTO) {
        collectService.addCollect(reqDTO);
        return Results.success();
    }

    @PostMapping("/getCollectListByUseId")
    public Result<IPage<CollectGetCollectListByUseIdRespDTO>> getCollectListByUseId(@RequestBody CollectGetCollectListByUserIdReqDTO reqDTO) {
        return Results.success(collectService.getCollectListByUseId(reqDTO));
    }

//    @PostMapping("/getTrainingListByCollect")
//    public Result<> getTrainingListByCollect(@RequestBody CollectGetCollectListByUserIdReqDTO reqDTO) {
//
//        return Results.success(collectService.getCollectListByUseId(reqDTO));
//    }

}
