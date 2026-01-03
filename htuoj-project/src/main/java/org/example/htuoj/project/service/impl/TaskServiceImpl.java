package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.Task;
import org.example.htuoj.common.dto.req.TaskAddTaskRepDTO;
import org.example.htuoj.common.dto.req.TaskGetAllRepDTO;
import org.example.htuoj.common.dto.resp.TaskGetAllRespDTO;
import org.example.htuoj.common.mapper.TaskMapper;
import org.example.htuoj.project.service.ITaskService;
import org.example.htuoj.common.utils.UserHolder;

import java.util.function.Function;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-15
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void addTask(TaskAddTaskRepDTO reqDTO) {
        Long userId = UserHolder.getUserId();
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(reqDTO.getTitle());
        task.setDescription(reqDTO.getDescription());
        task.setDeadline(reqDTO.getDeadline());
        save(task);
    }

    @Override
    public IPage<TaskGetAllRespDTO> getAll(TaskGetAllRepDTO reqDTO) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, UserHolder.getUserId());
        wrapper.eq(Task::getIsCompleted, false);
        wrapper.orderByAsc(Task::getDeadline);
        // 修改bug,如果deadline时间相同的时候，查询可能会不按照谁寻
        wrapper.orderByDesc(Task::getCreateTime);
        IPage<Task> result = taskMapper.selectPage(reqDTO, wrapper);
        IPage<TaskGetAllRespDTO> convert = result.convert(new Function<Task, TaskGetAllRespDTO>() {
            @Override
            public TaskGetAllRespDTO apply(Task task) {
                TaskGetAllRespDTO taskGetAllRespDTO = new TaskGetAllRespDTO();
                taskGetAllRespDTO.setTaskId(task.getTaskId());
                taskGetAllRespDTO.setTitle(task.getTitle());
                taskGetAllRespDTO.setDescription(task.getDescription());
//                taskGetAllRespDTO.setDeadline(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(task.getDeadline()));
                taskGetAllRespDTO.setDeadline(task.getDeadline());
                taskGetAllRespDTO.setIsCompleted(task.getIsCompleted());
                return taskGetAllRespDTO;
            }
        });
        return convert;


    }

    @Override
    public void completedTask(Long taskId) {
        LambdaUpdateWrapper<Task> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Task::getTaskId, taskId);
        wrapper.eq(Task::getUserId, UserHolder.getUserId());
        wrapper.set(Task::getIsCompleted, true);
        update(wrapper);
    }
}
