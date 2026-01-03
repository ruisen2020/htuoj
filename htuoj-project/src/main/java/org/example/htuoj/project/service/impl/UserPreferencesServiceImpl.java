package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.project.crawler.OJCrawler;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.UserPreferences;
import org.example.htuoj.common.dto.resp.UserPreferencesGetRespDTO;
import org.example.htuoj.common.mapper.UserPreferencesMapper;
import org.example.htuoj.project.service.IUserPreferencesService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.common.utils.LockConstant.USER_PREFERENCE_LOCK_PREFIX;


/**
 * <p>
 * 用户偏好表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-16
 */
@Service
public class UserPreferencesServiceImpl extends ServiceImpl<UserPreferencesMapper, UserPreferences> implements IUserPreferencesService {
    @Autowired
    private UserPreferencesMapper userPreferencesMapper;

    @Autowired
    private RedissonClient redissonClient;
    
    @Autowired
    private OJCrawler ojCrawler;

    @Override
    public UserPreferencesGetRespDTO getUserPreferencesInfo(Long userId) {
        LambdaQueryWrapper<UserPreferences> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPreferences::getUserId, userId);
        UserPreferences userPreferences = userPreferencesMapper.selectOne(wrapper);
        UserPreferencesGetRespDTO result = new UserPreferencesGetRespDTO();

        if (userPreferences != null)
            BeanUtils.copyProperties(userPreferences, result);
        System.out.println(result);
        return result;
    }

    @Override
    @Transactional
    public void updateDaily() throws IOException {
        List<UserPreferences> userPreferences = userPreferencesMapper.selectList(null);
        for (UserPreferences userPreference : userPreferences) {
            updateUserPreference(userPreference);
        }
    }
    private void updateUserPreference(UserPreferences userPreference) {
        try {
            boolean ok = false;
            if (StringUtils.isNotBlank(userPreference.getCodeforcesUserName())) {
                ok = true;
                userPreference.setCodeforcesAcceptCount(ojCrawler.getProblemCountByCodeforces(userPreference.getCodeforcesUserName()));
                userPreference.setCodeforcesRating(ojCrawler.getRatingByCodeforces(userPreference.getCodeforcesUserName()));
            }
            if (StringUtils.isNotBlank(userPreference.getAcwingUserId())) {
                ok = true;
                userPreference.setAcwingRating(ojCrawler.getRatingByAcWing(userPreference.getAcwingUserId()));
            }
            if (StringUtils.isNotBlank(userPreference.getAtcoderUserName())) {
                ok = true;
                userPreference.setAtcoderAcceptCount(ojCrawler.getProblemCountByAtCoder(userPreference.getAtcoderUserName()));
                userPreference.setAtcoderRating(ojCrawler.getRatingByAtCoder(userPreference.getAtcoderUserName()));
            }
            if (StringUtils.isNotBlank(userPreference.getNowcoderUserId())) {
                ok = true;
                userPreference.setNowcoderRating(ojCrawler.getRatingByNowcoder(userPreference.getNowcoderUserId()));
                userPreference.setNowcoderAcceptCount(ojCrawler.getProblemCountByNowcoder(userPreference.getNowcoderUserId()));
            }
            if (StringUtils.isNotBlank(userPreference.getLuoguUserId())) {
                ok = true;
                userPreference.setLuoguRating(ojCrawler.getRatingByLuogu(userPreference.getLuoguUserId()));
                userPreference.setLuoguAcceptCount(ojCrawler.getProblemCountByLuogu(userPreference.getLuoguUserId()));
            }
            if (StringUtils.isNotBlank(userPreference.getLeetcodeUserName())) {
                ok = true;
                userPreference.setLeetcodeRating(ojCrawler.getRatingByLeetcode(userPreference.getLeetcodeUserName()));
                userPreference.setLeetcodeAcceptCount(ojCrawler.getProblemCountByLeetcode(userPreference.getLeetcodeUserName()));
            }
            if (ok) {
                userPreference.setAcceptCount(userPreference.getAcwingAcceptCount()
                        + userPreference.getCodeforcesAcceptCount()
                        + userPreference.getLeetcodeAcceptCount()
                        + userPreference.getLuoguAcceptCount()
                        + userPreference.getNowcoderAcceptCount()
                        + userPreference.getAtcoderAcceptCount()
                        + userPreference.getAcceptEasyCount()
                        + userPreference.getAcceptMediumCount()
                        + userPreference.getAcceptHardCount());
                userPreferencesMapper.updateById(userPreference);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void updateUserPreference(UserPreferences userPreference) {
//        RLock lock = redissonClient.getLock(USER_PREFERENCE_LOCK_PREFIX + userPreference.getUserPreferencesId());
//        try {
//            boolean OK = lock.tryLock(1, 20, TimeUnit.SECONDS);
//            if (OK) {
//                UserPreferencesServiceImpl proxy = (UserPreferencesServiceImpl) AopContext.currentProxy();
//                proxy.updateUserPreference1(userPreference);
//            } else {
//                Thread.sleep(500);
//                this.updateUserPreference(userPreference);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ClientException("操作失败");
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

//    @Transactional
//    public void updateUserPreference1(UserPreferences userPreference) {
//        try {
//            boolean ok = false;
//            if (StringUtils.isNotBlank(userPreference.getCodeforcesUserName())) {
//                ok = true;
//                userPreference.setCodeforcesAcceptCount(ojCrawler.getProblemCountByCodeforces(userPreference.getCodeforcesUserName()));
//                userPreference.setCodeforcesRating(ojCrawler.getRatingByCodeforces(userPreference.getCodeforcesUserName()));
//            }
//            if (StringUtils.isNotBlank(userPreference.getAcwingUserId())) {
//                ok = true;
//                userPreference.setAcwingRating(ojCrawler.getRatingByAcWing(userPreference.getAcwingUserId()));
//            }
//            if (StringUtils.isNotBlank(userPreference.getAtcoderUserName())) {
//                ok = true;
//                userPreference.setAtcoderAcceptCount(ojCrawler.getProblemCountByAtCoder(userPreference.getAtcoderUserName()));
//                userPreference.setAtcoderRating(ojCrawler.getRatingByAtCoder(userPreference.getAtcoderUserName()));
//            }
//            if (StringUtils.isNotBlank(userPreference.getNowcoderUserId())) {
//                ok = true;
//                userPreference.setNowcoderRating(ojCrawler.getRatingByNowcoder(userPreference.getNowcoderUserId()));
//                userPreference.setNowcoderAcceptCount(ojCrawler.getProblemCountByNowcoder(userPreference.getNowcoderUserId()));
//            }
//            if (StringUtils.isNotBlank(userPreference.getLuoguUserId())) {
//                ok = true;
//                userPreference.setLuoguRating(ojCrawler.getRatingByLuogu(userPreference.getLuoguUserId()));
//                userPreference.setLuoguAcceptCount(ojCrawler.getProblemCountByLuogu(userPreference.getLuoguUserId()));
//            }
//            if (StringUtils.isNotBlank(userPreference.getLeetcodeUserName())) {
//                ok = true;
//                userPreference.setLeetcodeRating(ojCrawler.getRatingByLeetcode(userPreference.getLeetcodeUserName()));
//                userPreference.setLeetcodeAcceptCount(ojCrawler.getProblemCountByLeetcode(userPreference.getLeetcodeUserName()));
//            }
//            if (ok) {
//                userPreference.setAcceptCount(userPreference.getAcwingAcceptCount()
//                        + userPreference.getCodeforcesAcceptCount()
//                        + userPreference.getLeetcodeAcceptCount()
//                        + userPreference.getLuoguAcceptCount()
//                        + userPreference.getNowcoderAcceptCount()
//                        + userPreference.getAtcoderAcceptCount()
//                        + userPreference.getAcceptEasyCount()
//                        + userPreference.getAcceptMediumCount()
//                        + userPreference.getAcceptHardCount());
//                userPreferencesMapper.updateById(userPreference);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
