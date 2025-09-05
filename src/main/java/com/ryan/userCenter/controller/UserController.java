package com.ryan.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryan.userCenter.common.BaseResponse;
import com.ryan.userCenter.common.ErrorCode;
import com.ryan.userCenter.common.ResultUtils;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.domain.request.UserLoginRequest;
import com.ryan.userCenter.domain.request.UserRegisterRequest;
import com.ryan.userCenter.exception.BussinessException;
import com.ryan.userCenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

import static com.ryan.userCenter.constant.UserConstant.ADMIN_ROLE;
import static com.ryan.userCenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
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
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
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
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (id < 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.removeById(id));

    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = user.getId();
        //todo 校验用户是否合法
        User user1 = userService.getById(id);
        return ResultUtils.success(userService.getSafetyUser(user1));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogOut(request));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object userAttribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userAttribute;
        return user == null || user.getRole() != ADMIN_ROLE;
    }

}
