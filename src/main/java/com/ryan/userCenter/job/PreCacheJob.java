package com.ryan.userCenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PreCacheJob {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //todo 抓取用户数据，循环赋予redis键
    private final List<Long> mainUserList = List.of(1L);


    @Scheduled(cron = "0 */5 * * * *")
    public void doRecommendCache() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Page<User> page = userService.page(new Page<>(1, 20), wrapper);
        String format = String.format("ryan:user:recommend:%s", mainUserList);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        try {
            valueOperations.set(format, page, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.info("redis write error");
        }

    }
}
