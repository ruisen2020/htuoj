package org.example.htuoj.project.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.htuoj.common.dao.*;
import org.example.htuoj.common.mapper.*;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.service.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.*;

@Component
public class SpringRabbitListener {

    @Autowired
    private SubmissionMapper submissionMapper;

    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserPreferencesMapper userPreferencesMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AcceptStatusMapper acceptStatusMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ContestProblemMapper contestProblemMapper;

    @Autowired
    private ContestRegisterMapper contestRegisterMapper;

    @Autowired
    private IProblemService problemService;

    @Autowired
    private IContestProblemService contestProblemService;

    @Autowired
    private IContestRegisterService contestRegisterService;
    @Autowired
    private ContestAcceptMapper contestAcceptMapper;

    @Autowired
    private IContestAcceptService contestAcceptService;

    @Autowired
    private IContestService contestService;

    @RabbitListener(queues = "accept.queue")
    public void listenAcceptQueueMessage(String submissionId) {
        System.out.println("spring 消费者接收到消息：【" + submissionId + "】");
        Submission submission = submissionMapper.selectById(Long.parseLong(submissionId));
        synchronized (String.valueOf(submission.getUserId()).intern()) {
            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
            proxy.listenAcceptQueueMessageCore(submission);
        }

    }

    @Transactional
    public void listenAcceptQueueMessageCore(Submission submission) {
        // 1、修改acceptStatus,记录本次提交是否通过题目
        int flag = updateAcceptStatus(submission);
        // 2、更新题目的提交次数和通过次数
        updateProblem(submission);
        // 3、更新用户的过题数信息
        updateUserPreferences(submission, flag);

        // 4、增加提交竞赛题目的功能，修改比赛题目信息：提交次数，通过次数，用户罚时，用户过题数，题目是否通过，用户在该题目的提交次数
        if (submission.getContestId() != null) {
            // 如果是在比赛后提交的代码，不需要更改这些信息
            Contest contest = contestService.getById(submission.getContestId());
            if (!(submission.getCreateTime().before(contest.getStartTime()) || submission.getCreateTime().after(contest.getEndTime()))) {
                // 4.1、修改contest_problem 表
                updateContestProblem(submission);
                // 2.修改contest_register 和 contest_accept 表
                updateContestRegister(submission);
            }
        }
    }

    private void updateContestRegister(Submission submission) {
        LambdaQueryWrapper<ContestRegister> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContestRegister::getContestId, submission.getContestId());
        wrapper.eq(ContestRegister::getUserId, submission.getUserId());
        ContestRegister contestRegister = contestRegisterMapper.selectOne(wrapper);
        LambdaQueryWrapper<ContestAccept> contestAcceptWrapper = new LambdaQueryWrapper<>();
        contestAcceptWrapper.eq(ContestAccept::getUserId, submission.getUserId());
        contestAcceptWrapper.eq(ContestAccept::getContestId, submission.getContestId());
        contestAcceptWrapper.eq(ContestAccept::getProblemId, submission.getProblemId());
        ContestAccept contestAccept = contestAcceptMapper.selectOne(contestAcceptWrapper);
        // 如果题目通过了
        if (submission.getStatus() == 1) {
            // 如果题目没有通过才修改，已经通过的话不用修改。
            if (contestAccept.getIsAccept() == null || contestAccept.getIsAccept() == 0) {
                // 获取提交代码的时间
                Date createTime = submission.getCreateTime();
                // 获取题目开始时间
                Contest contest = contestService.getById(submission.getContestId());
                Date startTime = contest.getStartTime();
                // 计算两个时间差
                long penaltyTime = (createTime.getTime() - startTime.getTime()) / 1000 / 60;
                contestAcceptService.update().setSql("is_accept = 1")
                        .setSql("penalty_time = " + penaltyTime)
                        .setSql("submission_id = " + submission.getSubmissionId())
                        .eq("contest_accept_id", contestAccept.getContestAcceptId())
                        .update();
                long penaltyTimeSum = contestAccept.getWaSum() * 20 + penaltyTime;
                // 修改 contest_register 表
                contestRegisterService.update().setSql("penalty_time = penalty_time + " + penaltyTimeSum)
                        .setSql("accept_sum = accept_sum + 1")
                        .eq("contest_register_id", contestRegister.getContestRegisterId())
                        .update();
            }
        } else {
            // 如果未通过，那么该题目的不通过次数加1
            if (contestAccept.getIsAccept() == null || contestAccept.getIsAccept() == 0) {
                contestAcceptService.update().setSql("wa_sum = wa_sum + 1")
                        .setSql("is_accept = 0")
                        .eq("contest_accept_id", contestAccept.getContestAcceptId())
                        .update();
            }

        }
    }

    private void updateContestProblem(Submission submission) {
        if (submission.getStatus() == 1) {
            contestProblemService.update()
                    .setSql("accept_count = accept_count + 1")
                    .setSql("submit_count = submit_count + 1")
                    .eq("contest_id", submission.getContestId())
                    .eq("problem_id", submission.getProblemId())
                    .update();
        } else {
            contestProblemService.update()
                    .setSql("submit_count = submit_count + 1")
                    .eq("contest_id", submission.getContestId())
                    .eq("problem_id", submission.getProblemId())
                    .update();
        }
    }

    private void updateUserPreferences(Submission submission, int flag) {
        if (flag == 1) {
            Problem problem = problemMapper.selectById(submission.getProblemId());
            LambdaQueryWrapper<UserPreferences> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserPreferences::getUserId, submission.getUserId());
            UserPreferences userPreferences = userPreferencesMapper.selectOne(queryWrapper);
            userPreferences.setAcceptCount(userPreferences.getAcceptCount() + 1);
            if (problem.getDifficultyLevel() == 0) {
                userPreferences.setAcceptEasyCount(userPreferences.getAcceptEasyCount() + 1);
            } else if (problem.getDifficultyLevel() == 1) {
                userPreferences.setAcceptMediumCount(userPreferences.getAcceptMediumCount() + 1);
            } else if (problem.getDifficultyLevel() == 2) {
                userPreferences.setAcceptHardCount(userPreferences.getAcceptHardCount() + 1);
            }
            userPreferencesMapper.updateById(userPreferences);
        }
    }

    private void updateProblem(Submission submission) {
        if (submission.getStatus() == 1) {
            problemService.update().setSql("accept_count = accept_count + 1")
                    .setSql("submit_count = submit_count + 1")
                    .eq("id", submission.getProblemId())
                    .update();
        } else {
            problemService.update().setSql("submit_count = submit_count + 1")
                    .eq("id", submission.getProblemId())
                    .update();
        }
    }

    private int updateAcceptStatus(Submission submission) {
        // 修改当前题目是否通过
        int flag = 0;
        LambdaQueryWrapper<AcceptStatus> acceptStatusLambdaQueryWrapper = new LambdaQueryWrapper<>();
        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getUserId, submission.getUserId());
        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getProblemId, submission.getProblemId());
        AcceptStatus acceptStatus = acceptStatusMapper.selectOne(acceptStatusLambdaQueryWrapper);
        if (acceptStatus == null) {
            AcceptStatus acceptStatus1 = new AcceptStatus();
            acceptStatus1.setUserId(submission.getUserId());
            acceptStatus1.setProblemId(submission.getProblemId());
            if (submission.getStatus() == 1) {
                acceptStatus1.setStatus(1);
                flag = 1;
            } else {
                acceptStatus1.setStatus(2);
            }
            acceptStatusMapper.insert(acceptStatus1);
        } else {
            if (submission.getStatus() == 1 && acceptStatus.getStatus() != 1) {
                flag = 1;
                acceptStatus.setStatus(1);
            }
            acceptStatusMapper.updateById(acceptStatus);
        }
        return flag;
    }

//    @RabbitListener(queues = "accept.queue")
//    public void listenAcceptQueueMessage(String submissionId) {
//        System.out.println("spring 消费者接收到消息：【" + submissionId + "】");
//        stringRedisTemplate.opsForValue().set(SUBMISSION_LOCK_PREFIX + submissionId, "0");
//        Submission submission = submissionMapper.selectById(Long.parseLong(submissionId));
//        updateAcceptStatus(submission);
//        updateProblem(submission);
//        updateUserPreferences(submission);
//        // 增加提交竞赛题目的功能，修改比赛题目信息：提交次数，通过次数，用户罚时，用户过题数，题目是否通过，用户在该题目的提交次数
//        if (submission.getContestId() != null) {
//            // 如果是在比赛后提交的代码，不需要更改这些信息
//            Contest contest = contestService.getById(submission.getContestId());
//            if (!(submission.getCreateTime().before(contest.getStartTime()) || submission.getCreateTime().after(contest.getEndTime()))) {
//                // 1.修改contest_problem 表
//                updateContestProblem(submission);
//                // 2.修改contest_register 和 contest_accept 表
//                updateContestRegister(submission);
//            }
//        }
//    }

//    private void updateContestRegister(Submission submission) {
//        LambdaQueryWrapper<ContestRegister> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ContestRegister::getContestId, submission.getContestId());
//        wrapper.eq(ContestRegister::getUserId, submission.getUserId());
//        ContestRegister contestRegister = contestRegisterMapper.selectOne(wrapper);
//        LambdaQueryWrapper<ContestAccept> contestAcceptWrapper = new LambdaQueryWrapper<>();
//        contestAcceptWrapper.eq(ContestAccept::getUserId, submission.getUserId());
//        contestAcceptWrapper.eq(ContestAccept::getContestId, submission.getContestId());
//        contestAcceptWrapper.eq(ContestAccept::getProblemId, submission.getProblemId());
//        ContestAccept contestAccept = contestAcceptMapper.selectOne(contestAcceptWrapper);
//        RLock lock = redissonClient.getLock(CONTEST_REGISTER_LOCK_PREFIX + contestRegister.getContestRegisterId());
//        try {
//            // 修改当前题目提交次数和通过次数
//            lock.lock();
//            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
//            proxy.updateContestRegisterDetail(contestRegister, submission, contestAccept);
//        } catch (Exception e) {
//            throw new MessageConversionException("消息消费失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

    @Transactional
    public void updateContestRegisterDetail(ContestRegister contestRegister, Submission submission, ContestAccept contestAccept) {
        // 如果题目通过了
        if (submission.getStatus() == 1) {
            // 如果题目没有通过才修改，已经通过的话不用修改。
            if (contestAccept.getIsAccept() == null || contestAccept.getIsAccept() == 0) {
                // 获取提交代码的时间
                Date createTime = submission.getCreateTime();
                // 获取题目开始时间
                Contest contest = contestService.getById(submission.getContestId());
                Date startTime = contest.getStartTime();
                // 计算两个时间差
                long penaltyTime = (createTime.getTime() - startTime.getTime()) / 1000 / 60;
                contestAcceptService.update().setSql("is_accept = 1")
                        .setSql("penalty_time = " + penaltyTime)
                        .setSql("submission_id = " + submission.getSubmissionId())
                        .eq("contest_accept_id", contestAccept.getContestAcceptId())
                        .update();
                long penaltyTimeSum = contestAccept.getWaSum() * 20 + penaltyTime;
                // 修改 contest_register 表
                contestRegisterService.update().setSql("penalty_time = penalty_time + " + penaltyTimeSum)
                        .setSql("accept_sum = accept_sum + 1")
                        .eq("contest_register_id", contestRegister.getContestRegisterId())
                        .update();
            }
        } else {
            // 如果未通过，那么该题目的不通过次数加1
            if (contestAccept.getIsAccept() == null || contestAccept.getIsAccept() == 0) {
                contestAcceptService.update().setSql("wa_sum = wa_sum + 1")
                        .setSql("is_accept = 0")
                        .eq("contest_accept_id", contestAccept.getContestAcceptId())
                        .update();
            }

        }
    }

//    private void updateContestProblem(Submission submission) {
//        LambdaQueryWrapper<ContestProblem> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ContestProblem::getContestId, submission.getContestId());
//        wrapper.eq(ContestProblem::getProblemId, submission.getProblemId());
//        ContestProblem contestProblem = contestProblemMapper.selectOne(wrapper);
//        RLock lock = redissonClient.getLock(CONTEST_PROBLEM_LOCK_PREFIX + contestProblem.getContestProblemId());
//        try {
//            // 修改当前题目提交次数和通过次数
//            lock.lock();
//            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
//            proxy.updateContestProblemDetail(contestProblem, submission);
//        } catch (Exception e) {
//            throw new MessageConversionException("消息消费失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

//    @Transactional
//    public void updateContestProblemDetail(ContestProblem contestProblem, Submission submission) {
//        contestProblemService.update().setSql("submit_count = submit_count + 1")
//                .eq("contest_problem_id", contestProblem.getContestProblemId())
//                .update();
//        if (submission.getStatus() == 1) {
//            contestProblemService.update().setSql("accept_count = accept_count + 1")
//                    .eq("contest_problem_id", contestProblem.getContestProblemId())
//                    .update();
//        }
//    }

//    private void updateProblem(Submission submission) {
//        RLock lock = redissonClient.getLock(PROBLEM_LOCK_PREFIX + submission.getProblemId());
//        try {
//            // 修改当前题目提交次数和通过次数
//            lock.lock();
//            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
//            proxy.updateProblem1(submission);
//
//        } catch (Exception e) {
//            throw new MessageConversionException("消息消费失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//
//    }

    @Transactional
    public void updateProblem1(Submission submission) {
        problemService.update().setSql("submit_count = submit_count + 1")
                .eq("id", submission.getProblemId())
                .update();
        if (submission.getStatus() == 1) {
            problemService.update().setSql("accept_count = accept_count + 1")
                    .eq("id", submission.getProblemId())
                    .update();
        }
    }

//    private void updateAcceptStatus(Submission submission) {
//        // 修改当前题目是否通过
//        RLock lock = redissonClient.getLock(ACCEPT_STATUS_LOCK_PREFIX + submission.getUserId());
//        try {
//            lock.lock();
//            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
//            proxy.updateAcceptStatus1(submission);
//        } catch (Exception e) {
//            throw new MessageConversionException("消息消费失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

    @Transactional
    public void updateAcceptStatus1(Submission submission) {
        LambdaQueryWrapper<AcceptStatus> acceptStatusLambdaQueryWrapper = new LambdaQueryWrapper<>();
        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getUserId, submission.getUserId());
        acceptStatusLambdaQueryWrapper.eq(AcceptStatus::getProblemId, submission.getProblemId());
        AcceptStatus acceptStatus = acceptStatusMapper.selectOne(acceptStatusLambdaQueryWrapper);
        if (acceptStatus == null) {
            AcceptStatus acceptStatus1 = new AcceptStatus();
            acceptStatus1.setUserId(submission.getUserId());
            acceptStatus1.setProblemId(submission.getProblemId());
            if (submission.getStatus() == 1) {
                acceptStatus1.setStatus(1);
                stringRedisTemplate.opsForValue().set(SUBMISSION_LOCK_PREFIX + submission.getSubmissionId(), "1");
            } else {
                acceptStatus1.setStatus(2);
            }
            acceptStatusMapper.insert(acceptStatus1);
        } else {
            if (submission.getStatus() == 1 && acceptStatus.getStatus() != 1) {
                stringRedisTemplate.opsForValue().set(SUBMISSION_LOCK_PREFIX + submission.getSubmissionId(), "1");
                acceptStatus.setStatus(1);
            }
            acceptStatusMapper.updateById(acceptStatus);
        }
    }


//    private void updateUserPreferences(Submission submission) {
//        String s = stringRedisTemplate.opsForValue().get(SUBMISSION_LOCK_PREFIX + submission.getSubmissionId());
//        if (s != null && s.equals("0")) {
//            stringRedisTemplate.delete(SUBMISSION_LOCK_PREFIX + submission.getSubmissionId());
//            return;
//        }
//        stringRedisTemplate.delete(SUBMISSION_LOCK_PREFIX + submission.getSubmissionId());
//        RLock lock = redissonClient.getLock(USER_PREFERENCE_LOCK_PREFIX + UserHolder.getUserId());
//        try {
//            lock.lock();
//            SpringRabbitListener proxy = (SpringRabbitListener) AopContext.currentProxy();
//            proxy.updateUserPreferences1(submission);
//        } catch (Exception e) {
//            throw new MessageConversionException("消息消费失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//
//    }

//    @Transactional
//    public void updateUserPreferences1(Submission submission) {
//        Problem problem = problemMapper.selectById(submission.getProblemId());
//        LambdaQueryWrapper<UserPreferences> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(UserPreferences::getUserId, submission.getUserId());
//        UserPreferences userPreferences = userPreferencesMapper.selectOne(queryWrapper);
//        userPreferences.setAcceptCount(userPreferences.getAcceptCount() + 1);
//        if (problem.getDifficultyLevel() == 0) {
//            userPreferences.setAcceptEasyCount(userPreferences.getAcceptEasyCount() + 1);
//        } else if (problem.getDifficultyLevel() == 1) {
//            userPreferences.setAcceptMediumCount(userPreferences.getAcceptMediumCount() + 1);
//        } else if (problem.getDifficultyLevel() == 2) {
//            userPreferences.setAcceptHardCount(userPreferences.getAcceptHardCount() + 1);
//        }
//        userPreferencesMapper.updateById(userPreferences);
//    }
}