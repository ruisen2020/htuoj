package org.example.htuoj.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.htuoj.common.dao.Follow;
import org.example.htuoj.common.dao.School;
import org.example.htuoj.common.dao.User;
import org.example.htuoj.common.dao.UserPreferences;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.UserGetUserInfoRespDTO;
import org.example.htuoj.common.dto.resp.UserGetUserTopListRespDTO;
import org.example.htuoj.common.dto.resp.UserLoginGetInfoDTO;
import org.example.htuoj.common.dto.resp.UserLoginRespDTO;
import org.example.htuoj.common.mapper.SchoolMapper;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.project.utils.*;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.project.enums.UserErrorCodeEnum;
import org.example.htuoj.common.mapper.FollowMapper;
import org.example.htuoj.common.mapper.UserMapper;
import org.example.htuoj.common.mapper.UserPreferencesMapper;
import org.example.htuoj.project.service.IUserService;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.example.htuoj.project.enums.UserErrorCodeEnum.USER_ADD_ERROR;
import static org.example.htuoj.project.utils.RedisConstant.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssUtil ossUtil;

//    @Autowired
//    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private UserPreferencesMapper userPreferencesMapper;

    @Autowired
    private FollowMapper followMapper;

    // 布隆过滤器，用于在用户注册时，判断用户是否存在
    @Autowired
    private RBloomFilter<String> userNameBloomFilter;

//    @Autowired
//    private RBloomFilter<String> numberBloomFilter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public Long getUserIdByNumber(String number) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNumber, number);
        User user = userMapper.selectOne(wrapper);
        if (user != null)
            return user.getId();
        return null;
    }


    @Override
    public void register(UserAddReqDTO reqDTO) {
        // 1. 先判断验证码是否正确
        String code = stringRedisTemplate.opsForValue().get(REGISTER_CODE_KEY + reqDTO.getNumber());
        if (code == null) {
            throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_EXPIRED);
        }
        RLock lock = redissonClient.getLock(REGISTER_USERNAME_NUMBER_KEY + reqDTO.getName() + "-" + reqDTO.getNumber());
        try {
            boolean OK = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!OK) {
                throw new ClientException("用户名已存在");
            }
            if (!reqDTO.getCaptcha().equals(code)) {
                throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_ERROR);
            }
            // 1.检查用户名是否重复
            if (checkUserName(reqDTO.getName())) {
                throw new ClientException(UserErrorCodeEnum.USER_NAME_EXISTS);
            }
//            // 2.检查学号/工号是否重复
//            if (checkNumber(reqDTO.getNumber())) {
//                throw new ClientException(UserErrorCodeEnum.USER_EXISTS);
//            }
            UserServiceImpl proxy = (UserServiceImpl) AopContext.currentProxy();
            proxy.registerAffairs(reqDTO);
        } catch (Exception e) {
            if (e.getMessage().equals("用户名已存在")) {
                throw new ClientException("用户名已存在");
            } else if (e.getMessage().equals("学号已存在")) {
                throw new ClientException("学号已存在");
            } else {
                throw new ClientException(USER_ADD_ERROR);
            }
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            stringRedisTemplate.delete(REGISTER_USERNAME_NUMBER_KEY + reqDTO.getName() + "-" + reqDTO.getNumber());
        }
    }

    @Transactional
    public void registerAffairs(UserAddReqDTO reqDTO) {
        User user = new User();
        user.setUserName(reqDTO.getName());
        user.setPassword(reqDTO.getPassword());
        user.setNumber(reqDTO.getNumber());
        user.setMail(reqDTO.getMail());
        user.setLastLoginTime(new Date());
        userMapper.insert(user);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(user.getId());
        userPreferencesMapper.insert(userPreferences);
        userNameBloomFilter.add(user.getUserName());
//        numberBloomFilter.add(user.getNumber());
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO reqDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNumber, reqDTO.getNumber());
        wrapper.eq(User::getPassword, reqDTO.getPassword());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_LOGIN_ERROR);
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId());
        updateWrapper.set(User::getLastLoginTime, new Date());
        userMapper.update(null, updateWrapper);
//        String token = jwtUtils.generateToken(String.valueOf(user.getId()));
        UserLoginRespDTO userLoginRespDTO = new UserLoginRespDTO();
        StpUtil.login(user.getId());
        userLoginRespDTO.setToken(StpUtil.getTokenValue());
        UserLoginGetInfoDTO userLoginGetInfoDTO = new UserLoginGetInfoDTO();
        userLoginGetInfoDTO.setAvatar(user.getAvatar());
        userLoginGetInfoDTO.setUserName(user.getUserName());
        userLoginGetInfoDTO.setUserId(user.getId());
        userLoginGetInfoDTO.setNumber(user.getNumber());
        userLoginGetInfoDTO.setRealName(user.getRealName());
        userLoginRespDTO.setUserInfo(userLoginGetInfoDTO);
        return userLoginRespDTO;
    }

    @Override
    public String updateAvatar(MultipartFile avatar) {
        // 首先是上传头像
        String userAvatarPath = ossUtil.uploadAvatar(avatar);
        // 这里要改成从UserHolder中获取用户的id
        User user = new User();
        user.setId(UserHolder.getUserId());
        user.setAvatar(userAvatarPath);
        userMapper.updateById(user);
        return userAvatarPath;
    }

    @Override
    public IPage<UserGetUserInfoRespDTO> getAll(UserGetAllReqDTO reqDTO) {
        IPage<UserGetUserInfoRespDTO> result = userMapper.getAll(reqDTO);
        if (reqDTO.getOrderBy().equals("lastLoginTime")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime localDateTime = now.minusMinutes(5);
            Date futureDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            for (UserGetUserInfoRespDTO userGetUserInfoRespDTO : result.getRecords()) {
                if (userGetUserInfoRespDTO.getLastLoginTime().after(futureDate)) {
                    userGetUserInfoRespDTO.setOnlineStatus("在线");
                } else {
                    userGetUserInfoRespDTO.setOnlineStatus("离线");
                }
            }
        }
        return result;
    }

    @Override
    public String getAvatar() {
        Long userId = UserHolder.getUserId();
        if (userId == null) {
            return "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";
        }
        return userMapper.selectById(userId).getAvatar();
    }

    @Override
    public UserGetUserInfoRespDTO getUserInfoById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXISTS);
        }
        if (!Objects.equals(user.getId(), UserHolder.getUserId())) {
            user.setRealName(null);
        }
        UserGetUserInfoRespDTO userGetUserInfoRespDTO = new UserGetUserInfoRespDTO();
        School school = schoolMapper.selectById(user.getSchoolId());
        userGetUserInfoRespDTO.setSchoolName(school.getSchoolName());
        userGetUserInfoRespDTO.setSchoolId(school.getSchoolId());
        BeanUtils.copyProperties(user, userGetUserInfoRespDTO);
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowForm, UserHolder.getUserId());
        followLambdaQueryWrapper.eq(Follow::getFollowTo, id);
        Follow follow = followMapper.selectOne(followLambdaQueryWrapper);
        userGetUserInfoRespDTO.setIsFollowedByMe(follow != null);
        return userGetUserInfoRespDTO;
    }

    @Override
    public Void getCaptcha(String number) {
        String oldCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + number);
        // 1. 这里要判断验证码是否过期
        if (oldCode != null) {
            throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_NOT_EXPIRED);
        }
        // 1. 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 2.保存到Redis中
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + number, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 3.检查用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNumber, number);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXISTS);
        }
//        // 4.发送到用户的邮箱
        try {
            MailUtils.sendEmail(user.getMail(), code);
        } catch (Exception e) {
            throw new ClientException(UserErrorCodeEnum.USER_MAIL_ERROR);
        }
        return null;
    }

    @Override
    public void resetPassword(UserResetPasswordReqDTO reqDTO) {
        // 1. 先判断验证码是否正确
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + reqDTO.getNumber());
        if (code == null) {
            throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_EXPIRED);
        }
        if (!code.equals(reqDTO.getCaptcha())) {
            throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_ERROR);
        }
        // 2.更新密码
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getNumber, reqDTO.getNumber());
        userLambdaUpdateWrapper.set(User::getPassword, reqDTO.getNewPassword());
        userMapper.update(null, userLambdaUpdateWrapper);
        // 3.删除验证码
        stringRedisTemplate.delete(LOGIN_CODE_KEY + reqDTO.getNumber());

    }

    @Override
    public Void getCaptchaRegister(UserGetCaptchaRegisterReqDTO reqDTO) {
        String number = reqDTO.getNumber();
        String mail = reqDTO.getMail();
        String userName = reqDTO.getUserName();
        String oldCode = stringRedisTemplate.opsForValue().get(REGISTER_CODE_KEY + number);
        // 这里要判断验证码是否过期
        if (oldCode != null) {
            throw new ClientException(UserErrorCodeEnum.LOGIN_CODE_NOT_EXPIRED);
        }
        System.out.println(userName);
        // 1.检查用户名是否重复
        System.out.println(userNameBloomFilter.contains(userName));
        if (checkUserName(userName)) {
            System.out.println("用户名重复");
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        // 2.检查学号/工号是否重复
//        if (checkNumber(number)) {
//            System.out.println("学号/工号重复");
//            throw new ClientException(UserErrorCodeEnum.USER_EXISTS);
//        }

        // 3.检查用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNumber, number);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            throw new ClientException(UserErrorCodeEnum.USER_EXISTS);
        }
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getMail, mail);
        User user1 = userMapper.selectOne(wrapper);
        if (user1 != null) {
            throw new ClientException(UserErrorCodeEnum.USER_MAIL_EXISTS);
        }
        // 1. 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 2.保存到Redis中
        stringRedisTemplate.opsForValue().set(REGISTER_CODE_KEY + number, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);
        // 4.发送到用户的邮箱
        try {
            MailUtils.sendEmail(mail, code);
        } catch (Exception e) {
            throw new ClientException(UserErrorCodeEnum.USER_MAIL_ERROR);
        }
        return null;
    }

//    private boolean checkNumber(String number) {
//        return numberBloomFilter.contains(number);
//    }

    private boolean checkUserName(String userName) {
        return userNameBloomFilter.contains(userName);
    }

    @Override
    @Transactional
    public void updateUserAllInfo(UserUpdateAllInfoReqDTO reqDTO) {
        if (!Objects.equals(reqDTO.getUserInfo().getId(), UserHolder.getUserId())) {
            throw new ClientException("不能修改他人信息");
        }
        String userName = reqDTO.getUserInfo().getUserName();
        if (userName == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_NOT_NULL);
        }
        User user = userMapper.selectById(reqDTO.getUserInfo().getId());
        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXISTS);
        }

        if (!userName.equals(user.getUserName()) && checkUserName(userName)) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        UserInfo userInfo = reqDTO.getUserInfo();

        // 1. 更新用户信息
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, UserHolder.getUserId());
        wrapper.set(User::getProfile, userInfo.getProfile());
        wrapper.set(User::getRealName, userInfo.getRealName());
        wrapper.set(User::getUserName, userInfo.getUserName());
        wrapper.set(User::getSchoolId, userInfo.getSchoolId());
        userNameBloomFilter.add(userInfo.getUserName());
        int update = userMapper.update(null, wrapper);
        if (update != 1) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_USER_INFO_ERROR);
        }
        // 2. 更新用户偏好信息
        UserPreferencesInfo userPreferencesInfo = reqDTO.getUserPreferencesInfo();
        UserPreferences userPreferences = new UserPreferences();
        BeanUtils.copyProperties(userPreferencesInfo, userPreferences);
        LambdaUpdateWrapper<UserPreferences> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(UserPreferences::getUserId, UserHolder.getUserId());
        int update1 = userPreferencesMapper.update(userPreferences, wrapper1);

        if (update1 != 1) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_USER_INFO_ERROR);
        }

    }

    @Override
    public IPage<UserGetUserTopListRespDTO> getUserTopList(UserGetUserTopListReqDTO reqDTO) {
        IPage<UserGetUserTopListRespDTO> result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        // 1.先看redis中是否存在
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(USER_TOP_LIST_KEY);
        if (!entries.isEmpty()) {
            result = new Page<>();
            List<UserGetUserTopListRespDTO> records = new ArrayList<>();
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                try {
                    UserGetUserTopListRespDTO userGetUserTopListRespDTO = objectMapper.readValue((String) entry.getValue(), UserGetUserTopListRespDTO.class);
                    records.add(userGetUserTopListRespDTO);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            // 反转集合
            records.sort(Comparator.comparingInt(UserGetUserTopListRespDTO::getAcceptCount).reversed());
            result.setRecords(records);
        } else {
            // 2.不存在就查询
            // 需要将对象转换成JSON格式的字符串才能在redis中存储
            result = userMapper.getUserTopList(reqDTO);
            for (int i = 0; i < result.getRecords().size(); i++) {
                UserGetUserTopListRespDTO userGetUserTopListRespDTO = result.getRecords().get(i);
                try {
                    stringRedisTemplate.opsForHash().put(RedisConstant.USER_TOP_LIST_KEY, String.valueOf(i + 1), objectMapper.writeValueAsString(userGetUserTopListRespDTO));
                    stringRedisTemplate.expire(RedisConstant.USER_TOP_LIST_KEY, 1, TimeUnit.DAYS);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    @Override
    public void logout() {
        StpUtil.logout();
        Long userId = UserHolder.getUserId();
        stringRedisTemplate.delete(USER_TOKEN_PREFIX + userId);
        UserHolder.removeUser();
    }

    @Override
    public Void sendHeartbeat() {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getLastLoginTime, new Date());
        wrapper.eq(User::getId, UserHolder.getUserId());
        userMapper.update(null, wrapper);
        return null;
    }
}
