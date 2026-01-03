package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.Notice;
import org.example.htuoj.common.mapper.NoticeMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Follow;
import org.example.htuoj.common.dto.resp.FollowGetCountRespDTO;
import org.example.htuoj.common.dto.resp.FollowGetFollowInfo;
import org.example.htuoj.common.dto.resp.UserGetAllRespDTO;
import org.example.htuoj.common.mapper.FollowMapper;
import org.example.htuoj.project.service.IFollowService;
import org.example.htuoj.common.utils.UserHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.FOLLOW_LOCK_SUFFIX;

/**
 * <p>
 * 关注表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-09
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public FollowGetCountRespDTO getCount(Long id) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowForm, id);
        wrapper.eq(Follow::getState, 0);
        Long followCount = followMapper.selectCount(wrapper);
        LambdaQueryWrapper<Follow> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Follow::getFollowTo, id);
        wrapper1.eq(Follow::getState, 0);
        Long fanCount = followMapper.selectCount(wrapper1);
        FollowGetCountRespDTO result = new FollowGetCountRespDTO();
        result.setFollowCount(followCount);
        result.setFanCount(fanCount);
        return result;
    }

    @Override
    public FollowGetFollowInfo getFollowInfo(Long userId) {
        Long myUserId = UserHolder.getUserId();
        FollowGetFollowInfo result = new FollowGetFollowInfo();
        List<UserGetAllRespDTO> followList = followMapper.getFollowList(userId, myUserId);
        List<UserGetAllRespDTO> fansList = followMapper.getFansList(userId, myUserId);
        result.setFollowList(followList);
        result.setFansList(fansList);
        return result;
    }

    @Override
    public void followOther(Long userId) {
        Long myUserId = UserHolder.getUserId();
        if (Objects.equals(userId, myUserId)) {
            throw new ClientException("不能关注自己");
        }

        synchronized (String.valueOf(userId).intern()) {
            FollowServiceImpl followService = (FollowServiceImpl) AopContext.currentProxy();
            followService.followOtherCore(myUserId, userId);
        }
    }

//    @Override
//    public void followOther(Long userId) {
//        Long myUserId = UserHolder.getUserId();
//        if (Objects.equals(userId, myUserId)) {
//            throw new ClientException("不能关注自己");
//        }
//        RLock lock = redissonClient.getLock(FOLLOW_LOCK_SUFFIX + myUserId
//                + "-" + userId);
//        try {
//            boolean ok = lock.tryLock(1, 10, TimeUnit.SECONDS);
//            if (ok) {
//                FollowServiceImpl proxy = (FollowServiceImpl) AopContext.currentProxy();
//                proxy.followOther1(myUserId, userId);
//            } else {
//                Thread.sleep(500);
//                this.followOther(userId);
//            }
//        } catch (InterruptedException e) {
//            throw new ClientException("操作失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

    @Transactional
    public void followOtherCore(Long myUserId, Long userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowForm, myUserId);
        wrapper.eq(Follow::getFollowTo, userId);
        Follow one = followMapper.selectOne(wrapper);
        if (one == null) {
            Follow follow = new Follow();
            follow.setFollowForm(myUserId);
            follow.setFollowTo(userId);
            followMapper.insert(follow);
            addNotice(follow, true);
        } else {
            if (one.getState() == 1) {
                one.setState(0);
                addNotice(one, true);
            } else {
                one.setState(1);
                addNotice(one, false);
            }
            followMapper.updateById(one);
        }
    }

    private void addNotice(Follow follow, boolean isAdd) {
        if (isAdd) {
            Notice notice = new Notice();
            notice.setReceiverId(follow.getFollowTo());
            notice.setSenderId(follow.getFollowForm());
            notice.setTargetType(4);
            noticeMapper.insert(notice);
        } else {
            LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Notice::getSenderId, follow.getFollowForm());
            updateWrapper.eq(Notice::getReceiverId, follow.getFollowTo());
            updateWrapper.eq(Notice::getTargetType, 4);
            updateWrapper.setSql("del_flag = 1");
            noticeMapper.update(null, updateWrapper);
        }

    }
}
