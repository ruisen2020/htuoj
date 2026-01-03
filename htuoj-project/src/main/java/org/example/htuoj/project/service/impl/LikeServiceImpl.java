package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.*;
import org.example.htuoj.common.mapper.NoticeMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.htuoj.common.dto.req.LikeAddReqDTO;
import org.example.htuoj.common.mapper.LikeMapper;
import org.example.htuoj.project.service.IArticleService;
import org.example.htuoj.project.service.ICommentService;
import org.example.htuoj.project.service.ILikeService;
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
@Slf4j
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {
    @Autowired
    private LikeMapper likeMapper;


    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private NoticeMapper noticeMapper;

//    @Transactional
//    @Override
//    public void addLike(LikeAddReqDTO reqDTO) throws InterruptedException {
//        reqDTO.setUserId(UserHolder.getUserId());
//        reqDTO.setIsLike(!reqDTO.getIsLike());
//        reqDTO.setCreateTime(new Date());
//        reqDTO.setUpdateTime(new Date());
//        try {
//            likeMapper.addLike(reqDTO);
//        } catch (Exception e) {
//            throw new ClientException("操作失败");
//        }
//        reqDTO.setIsLike(!reqDTO.getIsLike());

    /// /        // 1、修改点赞表
    /// /        LambdaUpdateWrapper<Like> updateWrapper = new LambdaUpdateWrapper<>();
    /// /        updateWrapper.eq(Like::getTargetId, reqDTO.getTargetId());
    /// /        updateWrapper.eq(Like::getTargetType, reqDTO.getTargetType());
    /// /        updateWrapper.eq(Like::getUserId, UserHolder.getUserId());
    /// /        Integer state = reqDTO.getIsLike() ? 0 : 1;
    /// /        updateWrapper.set(Like::getState, state);
    /// /        int update = likeMapper.update(null, updateWrapper);
    /// /        if (update == 0) {
    /// /            // 插入点赞记录
    /// /            Like like = new Like();
    /// /            like.setUserId(UserHolder.getUserId());
    /// /            like.setTargetId(reqDTO.getTargetId());
    /// /            like.setTargetType(reqDTO.getTargetType());
    /// /            like.setState(reqDTO.getIsLike() ? 0 : 1);
    /// /            try {
    /// /                likeMapper.insert(like);
    /// /            } catch (Exception e) {
    /// /                throw new ClientException("操作失败");
    /// /            }
    /// /        }
//
//        if (reqDTO.getTargetType() == 0) {
//            if (reqDTO.getIsLike()) {
//                articleService.update().setSql("like_count = like_count + 1")
//                        .eq("article_id", reqDTO.getTargetId())
//                        .update();
//            } else {
//                articleService.update().setSql("like_count = like_count - 1")
//                        .eq("article_id", reqDTO.getTargetId())
//                        .update();
//            }
//        } else {
//            if (reqDTO.getIsLike()) {
//                commentService.update().setSql("like_count = like_count + 1")
//                        .eq("comment_id", reqDTO.getTargetId())
//                        .update();
//            } else {
//                commentService.update().setSql("like_count = like_count - 1")
//                        .eq("comment_id", reqDTO.getTargetId())
//                        .update();
//            }
//        }
//        addNotice(reqDTO);
//    }


    //    @Override
//    public void addLike(LikeAddReqDTO reqDTO) throws InterruptedException {
//        // 修改点赞信息
//        threadLocalFlag.set(false);
//        this.updateLike(reqDTO);
//
//        // 修改评论信息
//        if (reqDTO.getTargetType() == 1) {
//            this.updateComment(reqDTO);
//        }
//        // 修改文章信息
//        else if (reqDTO.getTargetType() == 0) {
//            this.updateArticle(reqDTO);
//        }
//        addNotice(reqDTO);
//    }
//
    @Override
    public void addLike(LikeAddReqDTO reqDTO) {
        // 首先检查文章或者评论是否存在
        if (reqDTO.getTargetType() == 0) {
            Article article = articleService.getById(reqDTO.getTargetId());
            if (article == null) {
                throw new ClientException("文章不存在");
            }
        }
        if (reqDTO.getTargetType() == 1) {
            Comment comment = commentService.getById(reqDTO.getTargetId());
            if (comment == null) {
                throw new ClientException("评论不存在");
            }
        }
        // 第一种方案：分布式锁
//        RLock lock = redissonClient.getLock(LIKE_LOCK_SUFFIX
//                + UserHolder.getUserId()
//                + "-" + reqDTO.getTargetId()
//                + "-" + reqDTO.getTargetType());
//        try {
//            lock.lock();
//            LikeServiceImpl proxy = (LikeServiceImpl) AopContext.currentProxy();
//            proxy.addLikeCore(reqDTO);
//        } catch (Exception e) {
//            throw new ClientException("操作失败");
//        } finally {
//            lock.unlock();
//        }
        // syn锁
        synchronized (String.valueOf(UserHolder.getUserId()).intern()) {
            LikeServiceImpl proxy = (LikeServiceImpl) AopContext.currentProxy();
            proxy.addLikeCore(reqDTO);
        }
    }

    @Transactional
    public void addLikeCore(LikeAddReqDTO reqDTO) {
        int flag = 0;
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, UserHolder.getUserId())
                .eq(Like::getTargetType, reqDTO.getTargetType())
                .eq(Like::getTargetId, reqDTO.getTargetId());
        Like one = likeMapper.selectOne(wrapper);
        if (one == null) {
            Like like = new Like();
            like.setUserId(UserHolder.getUserId());
            like.setTargetType(reqDTO.getTargetType());
            like.setTargetId(reqDTO.getTargetId());
            likeMapper.insert(like);
            flag = 1;
        } else {
            if (one.getState() == 1) {
                one.setState(0);
                flag = 1;
            } else {
                one.setState(1);
            }
            likeMapper.updateById(one);
        }

        if (reqDTO.getTargetType() == 0) {
            if (flag == 1) {
                articleService.update().setSql("like_count = like_count + 1")
                        .eq("article_id", reqDTO.getTargetId())
                        .update();
            } else {
                articleService.update().setSql("like_count = like_count - 1")
                        .eq("article_id", reqDTO.getTargetId())
                        .update();
            }
        } else {
            if (flag == 1) {
                commentService.update().setSql("like_count = like_count + 1")
                        .eq("comment_id", reqDTO.getTargetId())
                        .update();
            } else {
                commentService.update().setSql("like_count = like_count - 1")
                        .eq("comment_id", reqDTO.getTargetId())
                        .update();
            }
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
        } else if (reqDTO.getTargetType() == 1) {
            Comment comment = commentService.getById(reqDTO.getTargetId());
            receiverId = comment.getUserId();
            targetId = comment.getCommentId();
        }
        Integer noticeTargetType = null;
        if (targetType == 0) {
            noticeTargetType = 0;
        } else if (targetType == 1) {
            noticeTargetType = 1;
        }
        updateWrapper.eq(Notice::getSenderId, senderId);
        updateWrapper.eq(Notice::getReceiverId, receiverId);
        updateWrapper.eq(Notice::getTargetId, targetId);
        updateWrapper.eq(Notice::getTargetType, targetType);
        updateWrapper.setSql("del_flag = 1");
        noticeMapper.update(null, updateWrapper);
        // 2、插入新的消息
        if (flag == 1) {
            Notice notice = new Notice();
            notice.setSenderId(senderId);
            notice.setReceiverId(receiverId);
            notice.setTargetId(targetId);
            notice.setTargetType(targetType);
            noticeMapper.insert(notice);
        }
    }
//
//    public void updateArticle(LikeAddReqDTO reqDTO) {
//        RLock lock2 = redissonClient.getLock(ARTICLE_LOCK_PREFIX + reqDTO.getTargetId());
//        try {
//            boolean OK = lock2.tryLock(1, 10, TimeUnit.SECONDS);
//            if (OK) {
//                LikeServiceImpl proxy = (LikeServiceImpl) AopContext.currentProxy();
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
//    public void updateArticle1(LikeAddReqDTO reqDTO) {
//        if (threadLocalFlag.get()) {
//            articleService.update().setSql("like_count = like_count + 1")
//                    .eq("article_id", reqDTO.getTargetId())
//                    .update();
//        } else {
//            articleService.update().setSql("like_count = like_count - 1")
//                    .eq("article_id", reqDTO.getTargetId())
//                    .update();
//        }
//    }
//
//    public void updateComment(LikeAddReqDTO reqDTO) {
//        RLock lock1 = redissonClient.getLock(COMMENT_LOCK_PREFIX + reqDTO.getTargetId());
//        try {
//            boolean OK = lock1.tryLock(1, 10, TimeUnit.SECONDS);
//            if (OK) {
//                LikeServiceImpl proxy = (LikeServiceImpl) AopContext.currentProxy();
//                proxy.updateComment1(reqDTO);
//            } else {
//                Thread.sleep(500);
//                this.updateComment(reqDTO);
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
//    public void updateComment1(LikeAddReqDTO reqDTO) {
//        if (threadLocalFlag.get()) {
//            commentService.update().setSql("like_count = like_count + 1")
//                    .eq("comment_id", reqDTO.getTargetId())
//                    .update();
//        } else {
//            commentService.update().setSql("like_count = like_count - 1")
//                    .eq("comment_id", reqDTO.getTargetId())
//                    .update();
//        }
//    }
//
//
//    public void updateLike(LikeAddReqDTO reqDTO) {
//        RLock lock = redissonClient.getLock(LIKE_LOCK_SUFFIX
//                + UserHolder.getUserId()
//                + "-" + reqDTO.getTargetId()
//                + "-" + reqDTO.getTargetType());
//        try {
//            boolean OK = lock.tryLock(1, 10, TimeUnit.SECONDS);
//            if (OK) {
//                LikeServiceImpl proxy = (LikeServiceImpl) AopContext.currentProxy();
//                proxy.addLike1(reqDTO);
//            } else {
//                Thread.sleep(500);
//                this.updateLike(reqDTO);
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
//    public void addLike1(LikeAddReqDTO reqDTO) {
//        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Like::getUserId, UserHolder.getUserId())
//                .eq(Like::getTargetType, reqDTO.getTargetType())
//                .eq(Like::getTargetId, reqDTO.getTargetId());
//        Like one = likeMapper.selectOne(wrapper);
//        if (one == null) {
//            Like like = new Like();
//            like.setUserId(UserHolder.getUserId());
//            like.setTargetType(reqDTO.getTargetType());
//            like.setTargetId(reqDTO.getTargetId());
//            likeMapper.insert(like);
//            threadLocalFlag.set(true);
//        } else {
//            if (one.getState() == 1) {
//                one.setState(0);
//                threadLocalFlag.set(true);
//            } else {
//                one.setState(1);
//            }
//            likeMapper.updateById(one);
//        }
//    }
}