package com.xingyang.chat.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务接口
 *
 * @author XingYang
 */
public interface RedisService {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 是否成功
     */
    <T> boolean setCacheObject(final String key, final T value);

    /**
     * 缓存基本的对象，Integer、String、实体类等，并设置过期时间
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     * @return 是否成功
     */
    <T> boolean setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit);

    /**
     * 获取缓存的基本对象
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getCacheObject(final String key);

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     * @return 是否成功
     */
    boolean deleteObject(final String key);

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 删除的数量
     */
    long deleteObject(final Collection<String> collection);
} 