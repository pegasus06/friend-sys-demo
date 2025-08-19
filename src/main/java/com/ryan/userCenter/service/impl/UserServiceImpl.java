package com.ryan.userCenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.mapper.UserMapper;
import com.ryan.userCenter.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author RZ
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-08-19 14:44:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




