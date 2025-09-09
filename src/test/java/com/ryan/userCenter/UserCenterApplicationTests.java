package com.ryan.userCenter;

import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.mapper.UserMapper;

import com.ryan.userCenter.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableAutoConfiguration
@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        Assertions.assertEquals(5, users.size());
        users.forEach(System.out::println);
    }
    @Test
    void testInsert() {
        List<String> list = Arrays.asList("java","python");
        List<User> users = userService.searchUsersByTags(list);
        Assertions.assertEquals(1,users.size());

    }

}
