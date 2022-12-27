package com.disney.teams.cache.impl.redis.cluster;

import com.disney.teams.cache.CacheRuntimeException;
import com.disney.teams.cache.impl.AbstractCache;
import com.disney.teams.utils.type.ArrayUtils;
import redis.clients.jedis.JedisCluster;
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
public class ClusterRedisCache extends AbstractCache {

    public static final Long SUC_CODE = 1L;
    public static final String SUC_CODE_STR = "OK";

    private JedisCluster jedisCluster;

    ClusterRedisCache() {
    }

    void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(buildKey(key));
    }

    @Override
    public <T> T get(String key) {
        String s = jedisCluster.get(buildKey(key));
        return valueSerializer.unSerialize(s);
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        String s = jedisCluster.get(buildKey(key));
        return valueSerializer.unSerialize(s, clz);
    }

    @Override
    public <T> boolean add(String key, T value, int expiredSeconds) {
        if (expiredSeconds == 0) {
            throw new CacheRuntimeException("invalid expiredSeconds,must -1 or large than 0");
        }
        if (isValidExpiredTime(expiredSeconds)) {
            SetParams setParams = SetParams.setParams().nx().ex(expiredSeconds);
            String rs = jedisCluster.set(buildKey(key), valueSerializer.serialize(value), setParams);
            return SUC_CODE_STR.equals(rs);
        } else {
            Long rs = jedisCluster.setnx(buildKey(key), valueSerializer.serialize(value));
            return SUC_CODE.equals(rs);
        }
    }

    @Override
    public <T> boolean put(final String key, final T value, final int expiredSeconds) {
        if (expiredSeconds == 0) {
            throw new CacheRuntimeException("invalid expiredSeconds,must -1 or large than 0");
        }
        SetParams setParams;
        if (isValidExpiredTime(expiredSeconds)) {
            setParams = SetParams.setParams().exAt(expiredSeconds);
        } else {
            setParams = SetParams.setParams();
        }
        String valueStr = valueSerializer.serialize(value);
        String rs = jedisCluster.set(buildKey(key), valueStr, setParams);
        return SUC_CODE_STR.equals(rs);
    }

    @Override
    public boolean delete(String key) {
        Long ok = jedisCluster.del(buildKey(key));
        return SUC_CODE.equals(ok);
    }

    @Override
    public int delete(String... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return 0;
        }
        String[] nKeys = new String[keys.length];
        for (int i = 0, len = keys.length; i < len; ++i) {
            nKeys[i] = buildKey(keys[i]);
        }
        long count = jedisCluster.del(nKeys);
        return (int) count;
    }

    @Override
    public long incr(String key, long delta) {
        return jedisCluster.incrBy(buildKey(key), delta);
    }

    @Override
    public void expire(String key, int expiredSeconds) {
        if (isValidExpiredTime(expiredSeconds)) {
            throw new CacheRuntimeException("invalid expiredSeconds,must large than 0");
        }
        String buildKey = buildKey(key);
        for (int i = 0; i < 3; ++i) {
            Long result = jedisCluster.expire(buildKey, expiredSeconds);
            if (SUC_CODE.equals(result)) {
                return;
            } else {
                logger.warn("Set expire by key {} return error code {}", buildKey, result);
            }
        }
        logger.error("Set expire by key {} error retry count 3!", buildKey);
    }

    @Override
    public int ttl(String key) {
        long ttl = jedisCluster.ttl(buildKey(key));
        if (ttl == -2) {
            return CACHE_NOT_EXISTS;
        } else if (ttl == -1) {
            return CACHE_NO_EXPIRE;
        }
        return (int) ttl;
    }
}
