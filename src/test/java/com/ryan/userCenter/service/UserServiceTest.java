package com.ryan.userCenter.service;
import java.util.Date;

import com.ryan.userCenter.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("");
        user.setUseraccount("");
        user.setAvatarurl("");
        user.setGender(0);
        user.setUserpassword("");
        user.setPhone("");
        user.setEmail("");
        user.setUserstatus(0);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        user.setIsdelete(0);
        user.setUserrole(0);
        user.setPlanetcode("");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

}