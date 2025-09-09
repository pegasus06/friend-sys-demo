package com.ryan.userCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.userCenter.common.ErrorCode;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.exception.BusinessException;
import com.ryan.userCenter.mapper.UserMapper;
import com.ryan.userCenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ryan.userCenter.constant.UserConstant.USER_LOGIN_STATE;

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

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            //todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数过短");
        }
        if (password.length() < 4 || checkPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");

        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球代码过长");

        }
        String validateReg = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validateReg).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名包含特殊字符");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("planetCode", planetCode);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已存在用户");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encryptPassword);
        user.setPlanetcode(planetCode);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统错误");
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
        if (user == null) {
            log.info("用户名或密码错误");
            return null;
        }
        /**
         * 限流操作
         */
        //用户信息脱敏
        User safeUser = getSafetyUser(user);
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, user);
        return safeUser;
    }

    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUseraccount(user.getUseraccount());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setAvatarurl(user.getAvatarurl());
        safeUser.setCreatetime(user.getCreatetime());
        safeUser.setAvatarurl(user.getAvatarurl());
        safeUser.setGender(user.getGender());
        safeUser.setUserstatus(user.getUserstatus());
        safeUser.setUserrole(user.getUserrole());
        safeUser.setPlanetcode(user.getPlanetcode());
        return safeUser;
    }

    @Override
    public int userLogOut(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUsersByTags(List<String> tagNames) {
        if (CollectionUtils.isEmpty(tagNames)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        tagNames.forEach(
                tagName -> wrapper.or().like("tags", tagName)
        );
        return userMapper.selectList(wrapper);
    }


}




