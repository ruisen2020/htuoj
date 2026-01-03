package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Task;
import org.example.htuoj.common.dto.req.TaskAddTaskRepDTO;
import org.example.htuoj.common.dto.req.TaskGetAllRepDTO;
import org.example.htuoj.common.dto.resp.TaskGetAllRespDTO;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-15
 */
public interface ITaskService extends IService<Task> {

    void addTask( TaskAddTaskRepDTO reqDTO);

    IPage<TaskGetAllRespDTO> getAll(TaskGetAllRepDTO reqDTO);

    void completedTask(Long taskId);
}
