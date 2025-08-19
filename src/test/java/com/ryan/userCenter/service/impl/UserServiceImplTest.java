package com.ryan.userCenter.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserServiceImpl userService;
    @Test
    void userRegister() {
        long ryan = userService.userRegister("ryan", "123456", "123456");
        Assertions.assertNotEquals(-1,ryan);
    }


}