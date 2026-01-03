package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.TaskAddTaskRepDTO;
import org.example.htuoj.common.dto.req.TaskCompletedTaskReqDTO;
import org.example.htuoj.common.dto.req.TaskGetAllRepDTO;
import org.example.htuoj.common.dto.resp.TaskGetAllRespDTO;
import org.example.htuoj.project.service.ITaskService;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-15
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    @PostMapping("addTask")
    @SaCheckLogin
    public Result<Void> addTask(@RequestBody TaskAddTaskRepDTO reqDTO) {
        taskService.addTask(reqDTO);
        return Results.success();
    }

    @PostMapping("getAll")
    public Result<IPage<TaskGetAllRespDTO>> getAll(@RequestBody TaskGetAllRepDTO reqDTO) {
        return Results.success(taskService.getAll(reqDTO));
    }

    @PostMapping("completedTask")
    @SaCheckLogin
    public Result<Void> completedTask(@RequestBody TaskCompletedTaskReqDTO reqDTO) {
        taskService.completedTask(reqDTO.getTaskId());
        return Results.success();
    }


}
