package com.aochensoft.democommon.service.cache.impl;

import cn.hutool.json.JSONUtil;
import com.aochensoft.democommon.service.cache.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务层实现类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 13:34:14
 */
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value));
    }

    @Override
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), Duration.ofMillis(timeout));
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(s)) {
            return JSONUtil.toBean(s, clazz);
        } else {
            return null;
        }
    }

    @Override
    public void del(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
