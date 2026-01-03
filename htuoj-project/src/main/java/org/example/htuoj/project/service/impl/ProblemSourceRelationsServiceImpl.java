package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.ProblemSourceRelations;
import org.example.htuoj.common.mapper.ProblemSourceRelationsMapper;
import org.example.htuoj.project.service.IProblemSourceRelationsService;

/**
 * <p>
 * 题目来源关联表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Service
public class ProblemSourceRelationsServiceImpl extends ServiceImpl<ProblemSourceRelationsMapper, ProblemSourceRelations> implements IProblemSourceRelationsService {

}
