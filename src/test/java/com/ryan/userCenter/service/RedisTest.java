package com.ryan.userCenter.service;

import com.ryan.userCenter.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key1","value1");
        valueOperations.set("key2","value2");
        User user = new User();
        user.setId(1L);
        user.setUsername("rui");
        valueOperations.set("ryanuser","aaa");
        String demo1 = (String) valueOperations.get("key1");
        Assertions.assertEquals("value1",demo1);
    }
}
