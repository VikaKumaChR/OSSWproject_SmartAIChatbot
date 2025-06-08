package com.xingyang.chat.service.impl;

import com.xingyang.chat.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类
 *
 * @author XingYang
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> boolean setCacheObject(final String key, final T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Set cache object success, key: {}", key);
            return true;
        } catch (Exception e) {
            log.error("Set cache object failed, key: {}, error: {}", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public <T> boolean setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            log.debug("Set cache object with timeout success, key: {}, timeout: {}, timeUnit: {}", key, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("Set cache object with timeout failed, key: {}, error: {}", key, e.getMessage(), e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCacheObject(final String key) {
        try {
            T value = (T) redisTemplate.opsForValue().get(key);
            log.debug("Get cache object, key: {}, value exists: {}", key, value != null);
            return value;
        } catch (Exception e) {
            log.error("Get cache object failed, key: {}, error: {}", key, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean deleteObject(final String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("Delete object, key: {}, result: {}", key, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Delete object failed, key: {}, error: {}", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public long deleteObject(final Collection<String> collection) {
        try {
            Long result = redisTemplate.delete(collection);
            log.debug("Delete collection, size: {}, result: {}", collection.size(), result);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Delete collection failed, size: {}, error: {}", collection.size(), e.getMessage(), e);
            return 0;
        }
    }
} 