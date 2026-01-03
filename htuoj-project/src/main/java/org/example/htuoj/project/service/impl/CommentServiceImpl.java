package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.dao.Notice;
import org.example.htuoj.common.mapper.NoticeMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Comment;
import org.example.htuoj.common.dto.req.CommentAddReqDTO;
import org.example.htuoj.common.dto.req.CommentDeleteReqDTO;
import org.example.htuoj.common.dto.req.CommentGetListReqDTO;
import org.example.htuoj.common.dto.resp.CommentGetListRespDTO;
import org.example.htuoj.common.mapper.CommentMapper;
import org.example.htuoj.project.service.IArticleService;
import org.example.htuoj.project.service.ICommentService;
import org.example.htuoj.common.utils.UserHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.ARTICLE_LOCK_PREFIX;
import static org.example.htuoj.common.utils.LockConstant.COMMENT_LOCK_PREFIX;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private NoticeMapper noticeMapper;


    @Override
    public IPage<CommentGetListRespDTO> getCommentList(CommentGetListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        IPage<CommentGetListRespDTO> result = commentMapper.getCommentList(reqDTO);
        return result;
    }

    @Override
    @Transactional
    public void addComment(CommentAddReqDTO reqDTO) {
        // 1、判断文章存不存在
        Article article = articleService.getById(reqDTO.getArticleId());
        if (article == null) {
            throw new ClientException("文章不存在");
        }
        // 2、判断父评论存不存在
        if (reqDTO.getParentId() != null) {
            Comment comment = commentMapper.selectById(reqDTO.getParentId());
            if (comment == null) {
                throw new ClientException("父评论不存在");
            }
        }
        // 3、如果都满足才能创建
        Comment comment = new Comment();
        comment.setUserId(UserHolder.getUserId());
        comment.setContent(reqDTO.getContent());
        comment.setArticleId(reqDTO.getArticleId());
        comment.setParentId(reqDTO.getParentId());
        try {
            commentMapper.insert(comment);
        } catch (Exception e) {
            throw new ClientException("发表文章失败");
        }
        // 4、如果是子评论，则需要增加父评论的子评论数，同时也要增加文章的评论数
        updateCommentAndArticle(reqDTO, true);
        // 5、添加通知
        addNotice(comment, true);
    }

    private void updateCommentAndArticle(CommentAddReqDTO reqDTO, boolean isAdd) {
        // 1、如果是子评论，需要修改父评论的子评论数
        if (reqDTO.getParentId() != null) {
            String sql = isAdd ? "child_count = child_count + 1" : "child_count = child_count - 1";
            this.update()
                    .setSql(sql)
                    .eq("comment_id", reqDTO.getParentId())
                    .update();
        }
        // 2、修改文章的子评论数
        String sql = isAdd ? "comment_count = comment_count + 1" : "comment_count = comment_count - 1";
        articleService.update()
                .setSql(sql)
                .eq("article_id", reqDTO.getArticleId())
                .update();
    }

//    @Override
//    public void addComment(CommentAddReqDTO reqDTO) {
//        Comment comment = new Comment();
//        comment.setUserId(UserHolder.getUserId());
//        comment.setContent(reqDTO.getContent());
//        comment.setArticleId(reqDTO.getArticleId());
//        comment.setParentId(reqDTO.getParentId());
//        try {
//            commentMapper.insert(comment);
//        } catch (Exception e) {
//            throw new ClientException("发表文章失败");
//        }
//        // 添加通知
//        addNotice(comment, reqDTO, true,null);
//        // 如果是子评论，则需要加上父评论的子评论数
//        updateComment(reqDTO, true);
//        // 文章评论数+1
//        updateArticle(reqDTO, true);
//    }

    private void addNotice(Comment comment, boolean isAdd) {
        if (isAdd) {
            Notice notice = new Notice();
            notice.setSenderId(UserHolder.getUserId());
            if (comment.getParentId() == null) {
                Article article = articleService.getById(comment.getArticleId());
                notice.setReceiverId(article.getUserId());
                notice.setTargetId(comment.getCommentId());
                notice.setTargetType(2);
            } else {
                Comment parentComment = commentMapper.selectById(comment.getParentId());
                notice.setReceiverId(parentComment.getUserId());
                notice.setTargetId(comment.getCommentId());
                notice.setTargetType(3);
            }
            noticeMapper.insert(notice);
        } else {
            LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Notice::getSenderId, UserHolder.getUserId());
            if (comment.getParentId() == null) {
                Article article = articleService.getById(comment.getArticleId());
                updateWrapper.eq(Notice::getReceiverId, article.getUserId())
                        .eq(Notice::getTargetId, comment.getCommentId())
                        .eq(Notice::getTargetType, 2);
            } else {
                Comment parentComment = this.getById(comment.getParentId());
                updateWrapper.eq(Notice::getReceiverId, parentComment.getUserId())
                        .eq(Notice::getTargetId, comment.getCommentId())
                        .eq(Notice::getTargetType, 3);
            }
            updateWrapper.setSql("del_flag = 1");
            noticeMapper.update(null, updateWrapper);
        }
    }

//    private void updateArticle(CommentAddReqDTO reqDTO, boolean isAdd) {
//        if (isAdd) {
//            articleService.update().setSql("comment_count = comment_count + 1")
//                    .eq("article_id", reqDTO.getArticleId())
//                    .update();
//        } else {
//            articleService.update().setSql("comment_count = comment_count - 1")
//                    .eq("article_id", reqDTO.getArticleId())
//                    .update();
//        }
//    }

//    private void updateArticle(CommentAddReqDTO reqDTO, boolean isAdd) {
//        RLock lock = redissonClient.getLock(ARTICLE_LOCK_PREFIX + reqDTO.getArticleId());
//        try {
//            boolean ok = lock.tryLock(1, 10, TimeUnit.SECONDS);
//            if (ok) {
//                CommentServiceImpl proxy = (CommentServiceImpl) AopContext.currentProxy();
//                proxy.updateArticle1(reqDTO, isAdd);
//            } else {
//                Thread.sleep(500);
//                this.updateArticle(reqDTO, isAdd);
//            }
//        } catch (Exception e) {
//            throw new ClientException("发表评论失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

//    @Transactional
//    public void updateArticle1(CommentAddReqDTO reqDTO, boolean isAdd) {
//        if (isAdd) {
//            articleService.update().setSql("comment_count = comment_count + 1")
//                    .eq("article_id", reqDTO.getArticleId())
//                    .update();
//        } else {
//            articleService.update().setSql("comment_count = comment_count - 1")
//                    .eq("article_id", reqDTO.getArticleId())
//                    .update();
//        }
//
//    }

//    private void updateComment(CommentAddReqDTO reqDTO, boolean isAdd) {
//        if (reqDTO.getParentId() != null) {
//
//            RLock lock = redissonClient.getLock(COMMENT_LOCK_PREFIX + reqDTO.getParentId());
//            try {
//                boolean ok = lock.tryLock(1, 10, TimeUnit.SECONDS);
//                if (ok) {
//                    CommentServiceImpl proxy = (CommentServiceImpl) AopContext.currentProxy();
//                    proxy.updateComment1(reqDTO, isAdd);
//                } else {
//                    Thread.sleep(500);
//                    updateComment(reqDTO, isAdd);
//                }
//            } catch (Exception e) {
//                throw new ClientException("发表评论失败");
//            } finally {
//                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                    lock.unlock();
//                }
//            }
//        }
//    }

//    @Transactional
//    public void updateComment1(CommentAddReqDTO reqDTO, boolean isAdd) {
//        if (isAdd) {
//            this.update().setSql("child_count = child_count + 1")
//                    .eq("comment_id", reqDTO.getParentId())
//                    .update();
//        } else {
//            this.update().setSql("child_count = child_count - 1")
//                    .eq("comment_id", reqDTO.getParentId())
//                    .update();
//        }
//    }


    @Override
    @Transactional
    public void deleteComment(CommentDeleteReqDTO reqDTO) {
        Comment comment = commentMapper.selectById(reqDTO.getCommentId());
        if (!Objects.equals(comment.getUserId(), UserHolder.getUserId())) {
            throw new ClientException("不能删除别人的评论");
        }
        try {
            this.update()
                    .setSql("del_flag = 1")
                    .eq("comment_id", comment.getCommentId())
                    .update();
        } catch (Exception e) {
            throw new ClientException("删除评论失败");
        }
        // 如果是子评论，则需要减去父评论的子评论数
        CommentAddReqDTO commentAddReqDTO = new CommentAddReqDTO();
        commentAddReqDTO.setParentId(comment.getParentId());
        commentAddReqDTO.setArticleId(comment.getArticleId());
        //  修改评论和文章的数量
        updateCommentAndArticle(commentAddReqDTO, false);
        // 删除通知
        addNotice(comment, false);
    }
}
