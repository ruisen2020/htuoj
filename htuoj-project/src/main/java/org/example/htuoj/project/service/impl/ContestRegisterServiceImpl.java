package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Contest;
import org.example.htuoj.common.dao.ContestAccept;
import org.example.htuoj.common.dao.ContestProblem;
import org.example.htuoj.common.dao.ContestRegister;
import org.example.htuoj.common.dto.req.ContestRegisterAddReqDTO;
import org.example.htuoj.common.mapper.ContestAcceptMapper;
import org.example.htuoj.common.mapper.ContestProblemMapper;
import org.example.htuoj.common.mapper.ContestRegisterMapper;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.service.IContestRegisterService;
import org.example.htuoj.project.service.IContestService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.ARTICLE_LOCK_PREFIX;
import static org.example.htuoj.common.utils.LockConstant.CONTEST_LOCK_SUFFIX;

@Service
public class ContestRegisterServiceImpl extends ServiceImpl<ContestRegisterMapper, ContestRegister> implements IContestRegisterService {

    @Autowired
    private ContestRegisterMapper contestRegisterMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IContestService contestService;

    @Autowired
    private ContestProblemMapper contestProblemMapper;
    @Autowired
    private ContestAcceptMapper contestAcceptMapper;


    @Override
    @Transactional
    public void addRegister(ContestRegisterAddReqDTO reqDTO) {
        Contest contest = contestService.getById(reqDTO.getContestId());
        Date now = new Date();
        if (!now.after(contest.getEndTime())) {
            Long userId = UserHolder.getUserId();
            ContestRegister contestRegister = new ContestRegister();
            contestRegister.setContestId(reqDTO.getContestId());
            contestRegister.setUserId(userId);
            try {
                save(contestRegister);
                // 添加contest_accept表。
                insertContestAccept(reqDTO, userId);
            } catch (Exception e) {
                throw new ClientException("该竞赛已经报名");
            }
            contestService.update().setSql("register_sum = register_sum + 1")
                    .eq("contest_id", reqDTO.getContestId())
                    .update();
        } else {
            throw new ClientException("比赛已经结束");
        }
    }

    private void insertContestAccept(ContestRegisterAddReqDTO reqDTO, Long userId) {
        LambdaQueryWrapper<ContestProblem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContestProblem::getContestId, reqDTO.getContestId());
        wrapper.eq(ContestProblem::getDelFlag, 0);
        wrapper.select(ContestProblem::getProblemId);
        List<ContestProblem> contestProblems = contestProblemMapper.selectList(wrapper);
        for (ContestProblem contestProblem : contestProblems) {
            ContestAccept contestAccept = new ContestAccept();
            contestAccept.setUserId(userId);
            contestAccept.setContestId(reqDTO.getContestId());
            contestAccept.setProblemId(contestProblem.getProblemId());
            contestAcceptMapper.insert(contestAccept);
        }
    }

    @Override
    public void checkRegister(Long contestId) {
        Contest contest = contestService.getById(contestId);
        Date now = new Date();
        Date endTime = contest.getEndTime();
        Date startTime = contest.getStartTime();
        if (now.before(startTime)) {
            throw new ClientException("比赛尚未开始！");
        } else if (now.after(endTime)) {

        } else {
            LambdaQueryWrapper<ContestRegister> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ContestRegister::getUserId, UserHolder.getUserId());
            wrapper.eq(ContestRegister::getContestId, contestId);
            if (contestRegisterMapper.selectOne(wrapper) == null) {
                throw new ClientException("该竞赛未报名");
            }
        }
    }

//    private void updateContest(ContestRegisterAddReqDTO reqDTO) {
//        RLock lock = redissonClient.getLock(CONTEST_LOCK_SUFFIX + reqDTO.getContestId());
//        try {
//            lock.lock();
//            ContestRegisterServiceImpl proxy = (ContestRegisterServiceImpl) AopContext.currentProxy();
//            proxy.updateContest1(reqDTO);
//        } catch (Exception e) {
//            throw new ClientException("操作失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//    @Transactional
//    public void updateContest1(ContestRegisterAddReqDTO reqDTO) {
//        contestService.update().setSql("register_sum = register_sum + 1")
//                .eq("contest_id", reqDTO.getContestId())
//                .update();
//    }
}
