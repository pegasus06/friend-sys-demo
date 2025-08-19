package com.ryan.userCenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryan.userCenter.domain.User;

/**
* @author RZ
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-08-19 14:44:53
*/
public interface UserService extends IService<User> {

    long userRegister(String userName,String password,String checkPassword);
    User doLogin(String userName,String password);
}
