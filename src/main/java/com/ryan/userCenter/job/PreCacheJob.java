package com.ryan.userCenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //todo 抓取用户数据，循环赋予redis键
    private final List<Long> mainUserList = List.of(1L);


    @Scheduled(cron = "0 */5 * * * *")
    public void doRecommendCache() {
        RLock lock = redissonClient.getLock("ryan:precacheJob:doCache:lock");

        try {
            // 尝试获取锁，等待5秒，锁持有300秒后自动释放，看门狗机制
            if (lock.tryLock(5, 300, TimeUnit.SECONDS)) {
                for (long userId : mainUserList) {
                    QueryWrapper<User> wrapper = new QueryWrapper<>();
                    wrapper.eq("userId", userId);

                    Page<User> page = userService.page(new Page<>(1, 20), wrapper);

                    // 为每个用户生成独立的Redis键
                    String redisKey = String.format("ryan:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

                    try {
                        // 设置合理的过期时间（如10分钟）
                        valueOperations.set(redisKey, page, 10, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("redis write error for user: {}", userId, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
