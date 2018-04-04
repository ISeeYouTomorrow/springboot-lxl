package com.lxl.springmorecache.cache;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/30
 */
public class RedisCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private String id;
    private RedisTemplate redisTemplate;
    private static final long EXPIRE_TIME_IN_MINUTES = 30; // redis过期时间

    public RedisCache(String id) {
        if(id == null){
            throw  new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        try {
            redisTemplate = getRedisTemplate();
            ValueOperations ops = redisTemplate.opsForValue();
            ops.set(key,value, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
            logger.debug("Put query result to redis");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Put query result to redis error",e);
        }

    }

    @Override
    public Object getObject(Object key) {
        try {
            redisTemplate = getRedisTemplate();
            ValueOperations ops = redisTemplate.opsForValue();
            return ops.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get value from redis error",e);
        }
        return null;
    }

    @Override
    public Object removeObject(Object o) {
        try {
            redisTemplate = getRedisTemplate();
            redisTemplate.delete(o);
            logger.debug("Remove cached query result from redis:"+o);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Remove cached query result from redis error",e);
        }
        return o;
    }

    @Override
    public void clear() {
        redisTemplate = getRedisTemplate();
        redisTemplate.execute((RedisCallback)callBack->{
         callBack.flushDb();
         return "success";
        });
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return lock;
    }

    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }

}
