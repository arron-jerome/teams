package com.disney.teams.cache.impl;

import com.disney.teams.cache.CacheTimes;
import com.disney.teams.cache.ICache;
import com.disney.teams.cache.serializer.ValueSerializer;
import com.disney.teams.utils.type.ArrayUtils;
import com.disney.teams.utils.type.IntUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractCache implements ICache {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);

    private String prefixKey;

    /**
     * Timed out waiting for operation, ms
     */
    protected int defaultOpTimeout = 100;

    protected int defaultExpiredTime = CacheTimes.ONEDAY;

    protected ValueSerializer<String> valueSerializer;

    public void setValueSerializer(ValueSerializer<String> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public ValueSerializer<String> getValueSerializer() {
        return valueSerializer;
    }

    @Override
    public void setDefaultExpiredTime(int expiredSeconds) {
        this.defaultExpiredTime = expiredSeconds;
    }

    @Override
    public int getDefaultExpiredTime() {
        return defaultExpiredTime;
    }

    @Override
    public void setDefaultTimeOut(int timeout) {
        if (timeout >= 0) {
            defaultOpTimeout = timeout;
        }
    }

    @Override
    public int getDefaultTimeOut() {
        return defaultOpTimeout;
    }

    public String getPrefixKey() {
        return prefixKey;
    }

    public void setPrefixKey(String prefixKey) {
        this.prefixKey = prefixKey;
    }

    /**
     * 判断对应key值的ttl是否大于指定的ttl
     */
    private boolean isOverTtl(String key, Integer ttl) {
        //小于0或null时默认大于
        if (IntUtils.lt0(ttl)) {
            return true;
        }
        int t = ttl(key);
        if (t == CACHE_NO_EXPIRE) {
            return true;
        } else if (t == CACHE_NOT_EXISTS) {
            return false;
        } else {
            return t > ttl;
        }
    }

    @Override
    public <T> T getIfOverTtl(String key, Integer ttl) {
        return isOverTtl(key, ttl) ? get(key) : null;
    }

    @Override
    public <T> T getIfOverTtl(String key, Integer ttl, Class<T> clz) {
        return isOverTtl(key, ttl) ? get(key, clz) : null;
    }

    @Override
    public <T> boolean add(String key, T value) {
        return add(key, value, defaultExpiredTime);
    }

    @Override
    public <T> boolean addIfOverTtl(String key, T value, Integer ttl) {
        return addIfOverTtl(key, value, ttl, defaultExpiredTime);
    }

    @Override
    public <T> boolean addIfOverTtl(String key, T value, Integer ttl, int expiredSeconds) {
        if (ttl == null) {
            return add(key, value, expiredSeconds);
        }
        int t = ttl(key);
        if (t == CACHE_NO_EXPIRE) {
            return false;
        } else if (t == CACHE_NOT_EXISTS) {
            return add(key, value, expiredSeconds);
        } else if (t > ttl) {
            return false;
        }
        final String casKey = "addIfOverTtl^" + key;
        boolean added = add(casKey, 1, 10);
        if (!added) {
            return false;
        }
        try {
            if (isOverTtl(key, ttl)) {
                return false;
            }
            put(key, value, expiredSeconds);
            return true;
        } finally {
            delete(casKey);
        }
    }

    @Override
    public <T> boolean put(String key, T value) {
        return put(key, value, defaultExpiredTime);
    }

    @Override
    public int delete(String... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return 0;
        }
        int count = 0;
        for (String key : keys) {
            if (delete(key)) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public long decr(String key, long value) {
        return incr(key, -value);
    }

    protected String buildKey(String key) {
        return getPrefixKey() + key;
    }

    @Override
    public String getFullKey(String key) {
        return buildKey(key);
    }

    public boolean isValidExpiredTime(long expiredTime) {
        return expiredTime > 0;
    }
}
