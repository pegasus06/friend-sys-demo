package com.ryan.userCenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.userCenter.domain.UserTeam;
import com.ryan.userCenter.service.UserTeamService;
import com.ryan.userCenter.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author RZ
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2025-09-26 17:01:06
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




