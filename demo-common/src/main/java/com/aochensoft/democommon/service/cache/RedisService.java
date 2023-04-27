package com.aochensoft.democommon.service.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 13:33:44
 */
public interface RedisService {

    /**
     * 设置 redis 缓存
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置 redis 缓存指定时间(毫秒)
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间(毫秒)
     */
    void set(String key, Object value, long timeout);

    /**
     * 设置 redis 缓存指定时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取 redis 缓存
     *
     * @param key   键
     * @param clazz 值类型
     * @param <T>   值类型
     * @return 值
     */
    <T> T get(String key, Class<T> clazz);


    /**
     * 获取列表的 redis 缓存
     *
     * @param key   键
     * @param clazz 值类型
     * @param <T>   值类型
     * @return 列表类型的值
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 删除 redis 缓存
     *
     * @param key 键
     */
    void del(String key);
}
