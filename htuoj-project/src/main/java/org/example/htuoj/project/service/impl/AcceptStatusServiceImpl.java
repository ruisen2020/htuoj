package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.AcceptStatus;
import org.example.htuoj.common.mapper.AcceptStatusMapper;
import org.example.htuoj.project.service.IAcceptStatusService;

/**
 * <p>
 * 学生提交题目是否通过表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Service
public class AcceptStatusServiceImpl extends ServiceImpl<AcceptStatusMapper, AcceptStatus> implements IAcceptStatusService {

}

