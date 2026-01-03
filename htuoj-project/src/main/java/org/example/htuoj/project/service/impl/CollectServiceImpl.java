package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.*;
import org.example.htuoj.common.dto.req.CollectGetCollectListByUserIdReqDTO;
import org.example.htuoj.common.dto.req.LikeAddReqDTO;
import org.example.htuoj.common.dto.resp.CollectGetCollectListByUseIdRespDTO;
import org.example.htuoj.common.mapper.NoticeMapper;
import org.example.htuoj.project.service.ITrainingService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.htuoj.common.dto.req.CollectAddReqDTO;
import org.example.htuoj.common.mapper.CollectMapper;
import org.example.htuoj.project.service.IArticleService;
import org.example.htuoj.project.service.ICollectService;
import org.example.htuoj.common.utils.UserHolder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.*;

/**
 * <p>
 * 点赞表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ITrainingService trainingService;


    @Autowired
    private IArticleService articleService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private NoticeMapper noticeMapper;

    private final ThreadLocal<Boolean> threadLocalFlag = new ThreadLocal<>();

    @Override
    public void addCollect(CollectAddReqDTO reqDTO) {
        if (reqDTO.getTargetType() == 2) {
            Training training = trainingService.getById(reqDTO.getTargetId());
            if (training == null) {
                throw new ClientException("题单不存在");
            }
        } else if (reqDTO.getTargetType() == 0) {
            Article article = articleService.getById(reqDTO.getTargetId());
            if (article == null) {
                throw new ClientException("文章不存在");
            }
        }
        synchronized (String.valueOf(UserHolder.getUserId()).intern()) {
            CollectServiceImpl proxy = (CollectServiceImpl) AopContext.currentProxy();
            proxy.addCollectCore(reqDTO);
        }
    }

    @Transactional
    public void addCollectCore(CollectAddReqDTO reqDTO) {
        int flag = 0;
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, UserHolder.getUserId())
                .eq(Collect::getTargetId, reqDTO.getTargetId())
                .eq(Collect::getTargetType, reqDTO.getTargetType());
        Collect one = collectMapper.selectOne(wrapper);
        if (one == null) {
            Collect collect = new Collect();
            collect.setUserId(UserHolder.getUserId());
            collect.setTargetId(reqDTO.getTargetId());
            collect.setTargetType(reqDTO.getTargetType());
            collectMapper.insert(collect);
            flag = 1;
        } else {
            if (one.getState() == 1) {
                one.setState(0);
                flag = 1;
            } else {
                one.setState(1);
            }
            collectMapper.updateById(one);
        }
        String sql = flag == 1 ? "collect_count = collect_count + 1" : "collect_count = collect_count - 1";

        if (reqDTO.getTargetType() == 2) {
            trainingService.update().setSql(sql)
                    .eq("training_id", reqDTO.getTargetId())
                    .update();
        } else if (reqDTO.getTargetType() == 0) {
            articleService.update().setSql(sql)
                    .eq("article_id", reqDTO.getTargetId())
                    .update();
        }

        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
        Long senderId = UserHolder.getUserId();
        Long receiverId = null;
        Long targetId = null;
        Integer targetType = reqDTO.getTargetType();
        if (targetType == 0) {
            Article article = articleService.getById(reqDTO.getTargetId());
            receiverId = article.getUserId();
            targetId = article.getArticleId();
        } else if (reqDTO.getTargetType() == 2) {
            Training training = trainingService.getById(reqDTO.getTargetId());
            receiverId = training.getUserId();
            targetId = training.getTrainingId();
        }
        Integer noticeTargetType = null;
        if (targetType == 0) {
            noticeTargetType = 5;
        } else if (targetType == 2) {
            noticeTargetType = 6;
        }

        updateWrapper.eq(Notice::getSenderId, senderId);
        updateWrapper.eq(Notice::getReceiverId, receiverId);
        updateWrapper.eq(Notice::getTargetId, targetId);
        updateWrapper.eq(Notice::getTargetType, noticeTargetType);
        updateWrapper.setSql("del_flag = 1");
        noticeMapper.update(null, updateWrapper);
        // 2、插入新的消息
        if (flag == 1) {
            Notice notice = new Notice();
            notice.setSenderId(senderId);
            notice.setReceiverId(receiverId);
            notice.setTargetId(targetId);
            notice.setTargetType(noticeTargetType);
            noticeMapper.insert(notice);
        }
    }

    //    @Override
//    public void addCollect(CollectAddReqDTO reqDTO) {
//        threadLocalFlag.set(false);
//        updateCollect(reqDTO);
//        if (reqDTO.getTargetType() == 2) {
//            updateTraining(reqDTO);
//        } else if (reqDTO.getTargetType() == 0) {
//            updateArticle(reqDTO);
//        }
//        addNotice(reqDTO);
//    }
//    private void addNotice(CollectAddReqDTO reqDTO) {
//        if (threadLocalFlag.get()) {
//            Notice notice = new Notice();
//            notice.setSenderId(UserHolder.getUserId());
//            if (reqDTO.getTargetType() == 0) {
//                Article article = articleService.getById(reqDTO.getTargetId());
//                notice.setReceiverId(article.getUserId());
//                notice.setTargetId(article.getArticleId());
//                notice.setTargetType(5);
//            } else if (reqDTO.getTargetType() == 2) {
//                Training training = trainingService.getById(reqDTO.getTargetId());
//                notice.setReceiverId(training.getUserId());
//                notice.setTargetId(training.getTrainingId());
//                notice.setTargetType(6);
//            }
//            notice.setCreateTime(new Date());
//            noticeMapper.insert(notice);
//        } else {
//            LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.eq(Notice::getSenderId, UserHolder.getUserId());
//            if (reqDTO.getTargetType() == 0) {
//                Article article = articleService.getById(reqDTO.getTargetId());
//                updateWrapper.eq(Notice::getReceiverId, article.getUserId())
//                        .eq(Notice::getTargetId, article.getArticleId())
//                        .eq(Notice::getTargetType, 5);
//            } else if (reqDTO.getTargetType() == 2) {
//                Training training = trainingService.getById(reqDTO.getTargetId());
//                updateWrapper.eq(Notice::getReceiverId, training.getUserId())
//                        .eq(Notice::getTargetId, training.getTrainingId())
//                        .eq(Notice::getTargetType, 6);
//            }
//            updateWrapper.setSql("del_flag = 1");
//            noticeMapper.update(null, updateWrapper);
//        }
//    }

//    private void updateArticle(CollectAddReqDTO reqDTO) {
//        RLock lock2 = redissonClient.getLock(ARTICLE_LOCK_PREFIX + reqDTO.getTargetId());
//        try {
//            boolean ok = lock2.tryLock(1, 10, TimeUnit.SECONDS);
//            if (ok) {
//                CollectServiceImpl proxy = (CollectServiceImpl) AopContext.currentProxy();
//                proxy.updateArticle1(reqDTO);
//            } else {
//                Thread.sleep(500);
//                this.updateArticle(reqDTO);
//            }
//        } catch (Exception e) {
//            throw new ClientException("操作失败");
//        } finally {
//            if (lock2.isLocked() && lock2.isHeldByCurrentThread()) {
//                lock2.unlock();
//            }
//        }
//    }
//
//    @Transactional
//    public void updateArticle1(CollectAddReqDTO reqDTO) {
//        if (threadLocalFlag.get()) {
//            articleService.update().setSql("collect_count = collect_count + 1")
//                    .eq("article_id", reqDTO.getTargetId())
//                    .update();
//        } else {
//            articleService.update().setSql("collect_count = collect_count - 1")
//                    .eq("article_id", reqDTO.getTargetId())
//                    .update();
//        }
//    }
//
//    private void updateTraining(CollectAddReqDTO reqDTO) {
//        RLock lock1 = redissonClient.getLock(TRAINING_LOCK_SUFFIX + reqDTO.getTargetId());
//        try {
//            boolean ok = lock1.tryLock(1, 10, TimeUnit.SECONDS);
//            if (ok) {
//                CollectServiceImpl proxy = (CollectServiceImpl) AopContext.currentProxy();
//                proxy.updateTraining1(reqDTO);
//            } else {
//                Thread.sleep(500);
//                this.updateTraining(reqDTO);
//            }
//        } catch (Exception e) {
//            throw new ClientException("操作失败");
//        } finally {
//            if (lock1.isLocked() && lock1.isHeldByCurrentThread()) {
//                lock1.unlock();
//            }
//        }
//    }
//
//    @Transactional
//    public void updateTraining1(CollectAddReqDTO reqDTO) {
//        if (threadLocalFlag.get()) {
//            trainingService.update().setSql("collect_count = collect_count + 1")
//                    .eq("training_id", reqDTO.getTargetId())
//                    .update();
//        } else {
//            trainingService.update().setSql("collect_count = collect_count - 1")
//                    .eq("training_id", reqDTO.getTargetId())
//                    .update();
//        }
//    }
//
//    private void updateCollect(CollectAddReqDTO reqDTO) {
//        RLock lock = redissonClient.getLock(COLLECT_LOCK_SUFFIX + UserHolder.getUserId()
//                + "-" + reqDTO.getTargetId()
//                + "-" + reqDTO.getTargetType());
//        try {
//            boolean OK = lock.tryLock(1, 10, TimeUnit.SECONDS);
//            if (OK) {
//                CollectServiceImpl proxy = (CollectServiceImpl) AopContext.currentProxy();
//                proxy.addCollect1(reqDTO);
//            } else {
//                Thread.sleep(500);
//                this.updateCollect(reqDTO);
//            }
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
//    public void addCollect1(CollectAddReqDTO reqDTO) {
//        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Collect::getUserId, UserHolder.getUserId())
//                .eq(Collect::getTargetId, reqDTO.getTargetId())
//                .eq(Collect::getTargetType, reqDTO.getTargetType());
//        Collect one = collectMapper.selectOne(wrapper);
//        if (one == null) {
//            // 判断收藏的题单是不是自己的，如果是自己的不允许收藏
//            if (reqDTO.getTargetType() == 2) {
//                Training byId = trainingService.getById(reqDTO.getTargetId());
//                if (byId == null) {
//                    throw new ClientException("收藏对象不存在");
//                }
//                Long trainingId = byId.getTrainingId();
//                if (trainingId.equals(UserHolder.getUserId())) {
//                    throw new ClientException("不能收藏自己的题单");
//                }
//            }
//            Collect collect = new Collect();
//            collect.setUserId(UserHolder.getUserId());
//            collect.setTargetId(reqDTO.getTargetId());
//            collect.setTargetType(reqDTO.getTargetType());
//            collectMapper.insert(collect);
//            threadLocalFlag.set(true);
//        } else {
//            if (one.getState() == 1) {
//                one.setState(0);
//                threadLocalFlag.set(true);
//            } else {
//                one.setState(1);
//            }
//            collectMapper.updateById(one);
//        }
//    }


    @Override
    public IPage<CollectGetCollectListByUseIdRespDTO> getCollectListByUseId(CollectGetCollectListByUserIdReqDTO reqDTO) {
        reqDTO.setMyUserId(UserHolder.getUserId());
        return collectMapper.getCollectListByUseId(reqDTO);
    }
}
