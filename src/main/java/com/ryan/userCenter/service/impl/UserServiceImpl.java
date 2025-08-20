package com.ryan.userCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.mapper.UserMapper;
import com.ryan.userCenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RZ
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-08-19 14:44:53
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "rui";
    private final UserMapper userMapper;
    private static final String USER_LOGIN_STATE="loginState";

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (password.length() < 4 || checkPassword.length() < 4) {
            return -1;
        }
        String validateReg = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validateReg).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        if (!password.equals(checkPassword)) {
            return -1;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String password, HttpServletRequest httpServletRequest) {
        if (StringUtils.isAnyBlank(userAccount, password)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (password.length() < 4) {
            return null;
        }
        String validateReg = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validateReg).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user==null){
            log.info("用户名或密码错误");
            return null;
        }
        User safeUser = new User();
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE,user);
        return user;
    }
}




