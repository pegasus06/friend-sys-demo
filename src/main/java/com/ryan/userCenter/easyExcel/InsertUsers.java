package com.ryan.userCenter.easyExcel;

import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.mapper.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class InsertUsers {
    private UserMapper userMapper;
    @Scheduled(cron = "0 */5 * * * *")
    public void insertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i <= INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假沙鱼");
            user.setUseraccount("yusha");
            user.setAvatarurl("shanghai.myqcloud.com/shayu931/shayu.png");
            user.setProfile("一条咸鱼");
            user.setGender(0);
            user.setUserpassword("12345678");
            user.setPhone("123456789108");
            user.setEmail("shayu-yusha@qq.com");
            user.setUserstatus(0);
            user.setUserrole(0);
            user.setPlanetcode("931");
            user.setTags("[]");
            userMapper.insert(user);
        }
        stopWatch.stop();
    }
}
