package com.ryan.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryan.userCenter.common.BaseResponse;
import com.ryan.userCenter.common.ErrorCode;
import com.ryan.userCenter.common.ResultUtils;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.domain.request.UserLoginRequest;
import com.ryan.userCenter.domain.request.UserRegisterRequest;
import com.ryan.userCenter.exception.BusinessException;
import com.ryan.userCenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ryan.userCenter.constant.UserConstant.ADMIN_ROLE;
import static com.ryan.userCenter.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);

    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> collect = userList.stream().map(user -> {
            user.setUserpassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody int id, HttpServletRequest request) {
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.removeById(id));

    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = user.getId();
        //todo 校验用户是否合法
        User user1 = userService.getById(id);
        return ResultUtils.success(userService.getSafetyUser(user1));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogOut(request));
    }

    @PostMapping("/search/tags")
    public BaseResponse<List<User>> searchTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> users = userService.searchUsersByTags(tags);
        return ResultUtils.success(users);
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> getRecommend(long pageSize, long pageNum, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String key = String.format("ryan:user:recommend:%s", loginUser.getId());
        ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
        //如果有缓存，直接读取
        Page<User> userPage = (Page<User>) opsForValue.get(key);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageSize, pageNum), wrapper);
        //写入缓存
        try {
            opsForValue.set(key, userPage, 300000, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("redis key invalid",e);
        }
        return ResultUtils.success(userPage);
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object userAttribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userAttribute;
        return user == null || user.getUserrole() != ADMIN_ROLE;
    }

}
