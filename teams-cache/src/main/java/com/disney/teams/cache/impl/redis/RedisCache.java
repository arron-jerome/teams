package com.disney.teams.cache.impl.redis;

import com.disney.teams.cache.CacheRuntimeException;
import com.disney.teams.cache.impl.AbstractCache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/20
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/20       arron.zhou      1.0.0          create
 */
public class RedisCache extends AbstractCache {

    public static final Long SUC_CODE = 1L;
    public static final String SUC_CODE_STR = "OK";

    private JedisPool jedisPool;

    RedisCache() {
    }

    public RedisCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public boolean exists(String key) {
        try (Jedis ignored = jedisPool.getResource()) {
            return ignored.exists(buildKey(key));
        }
    }

    @Override
    public <T> T get(String key) {
        try (Jedis ignored = jedisPool.getResource()) {
            String s = ignored.get(buildKey(key));
            return valueSerializer.unSerialize(s);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        try (Jedis ignored = jedisPool.getResource()) {
            String s = ignored.get(buildKey(key));
            return valueSerializer.unSerialize(s, clz);
        }
    }

    @Override
    public <T> boolean add(String key, T value, int expiredSeconds) {
        if (expiredSeconds == 0) {
            throw new CacheRuntimeException("invalid expiredSeconds,must -1 or large than 0");
        }
        try (Jedis ignored = jedisPool.getResource()) {
            if (isValidExpiredTime(expiredSeconds)) {
                SetParams setParams = SetParams.setParams().ex(expiredSeconds)
                        .nx();
                String rs = ignored.set(buildKey(key)
                        , valueSerializer.serialize(value), setParams);
                return SUC_CODE_STR.equals(rs);
            } else {
                Long rs = ignored.setnx(buildKey(key), valueSerializer.serialize(value));
                return SUC_CODE.equals(rs);
            }
        }
    }

    @Override
    public <T> boolean put(final String key, final T value, final int expiredSeconds) {
        if (expiredSeconds == 0) {
            throw new CacheRuntimeException("invalid expiredSeconds,must -1 or large than 0");
        }
        try (Jedis ignored = jedisPool.getResource()) {
            SetParams setParams;
            if (isValidExpiredTime(expiredSeconds)) {
                setParams = SetParams.setParams().exAt(expiredSeconds);
            } else {
                setParams = SetParams.setParams();
            }
            String valueStr = valueSerializer.serialize(value);
            String rs = ignored.set(buildKey(key), valueStr, setParams);
            return SUC_CODE_STR.equals(rs);
        }
    }

    @Override
    public boolean delete(String key) {
        try (Jedis ignored = jedisPool.getResource()) {
            Long ok = ignored.del(buildKey(key));
            return SUC_CODE.equals(ok);
        }
    }

    @Override
    public long incr(final String key, final long value) {
        try (Jedis ignored = jedisPool.getResource()) {
            return ignored.incrBy(buildKey(key), value);
        }
    }

    @Override
    public void expire(String key, int expiredSeconds) {
        if (isValidExpiredTime(expiredSeconds)) {
            throw new CacheRuntimeException("invalid expiredSeconds,must large than 0");
        }
        try (Jedis ignored = jedisPool.getResource()) {
            String targetKey = buildKey(key);
            for (int i = 0; i < 3; ++i) {
                Long result = ignored.expire(targetKey, expiredSeconds);
                if (SUC_CODE.equals(result)) {
                    return;
                } else {
                    logger.warn("Set expire by key {} return error code {}", targetKey, result);
                }
            }
            logger.error("Set expire by key {} error retry count 3!", targetKey);
        }
    }

    @Override
    public int ttl(String key) {
        try (Jedis ignored = jedisPool.getResource()) {
            long ttl = ignored.ttl(buildKey(key));
            if (ttl == -2) {
                return CACHE_NOT_EXISTS;
            } else if (ttl == -1) {
                return CACHE_NO_EXPIRE;
            }
            return (int) ttl;
        }
    }
}
