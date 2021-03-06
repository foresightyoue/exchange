package com.huagu.vcoin.main.service.comm.redis;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * redis cache 工具类
 * 
 */
public class RedisUtil {
    // private static final Logger log = Logger.getLogger(RedisUtil.class);
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 批量删除对应的value
     * 
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * 
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     * 
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * 
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * 
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public double getDouble(final String key){
        Double tmp = (Double) get(key);
        return tmp != null ? tmp : 0;
    }
    
    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // map
    public boolean setMap(final String key1, String key2, Object value) {
        boolean result = false;
        try {
            HashOperations<Serializable, String, Object> operations = redisTemplate.opsForHash();
            operations.put(key1, key2, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object getMap(final String key1, String key2) {
        Object result = null;
        try {
            HashOperations<Serializable, String, Object> operations = redisTemplate.opsForHash();
            result = operations.get(key1, key2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteMap(final String key1, String key2) {
        boolean result = false;
        try {
            HashOperations<Serializable, String, Object> operations = redisTemplate.opsForHash();
            operations.delete(key1, key2);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}