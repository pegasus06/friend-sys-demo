package com.ryan.userCenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryan.userCenter.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author RZ
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-08-19 14:44:53
*/
public interface UserService extends IService<User> {


    long userRegister(String userName,String password,String checkPassword,String planetCode);
    User doLogin(String userName, String password, HttpServletRequest httpServlet);

    User getSafetyUser(User user);
    int userLogOut(HttpServletRequest httpServletRequest);
    List<User> searchUsersByTags(List<String> tagNames);
}
