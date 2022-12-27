package com.disney.teams.cache.utils;

import com.alibaba.fastjson.JSON;
import com.disney.teams.cache.CacheRuntimeException;
import com.disney.teams.cache.ICache;
import com.disney.teams.cache.impl.AbstractCache;
import com.disney.teams.cache.impl.redis.cluster.ClusterRedisCache;
import com.disney.teams.cache.impl.redis.RedisCache;
import com.disney.teams.cache.serializer.ValueSerializer;
import com.disney.teams.log.timer.AutoTimeLog;
import com.disney.teams.utils.type.MapUtils;
import com.disney.teams.utils.type.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.commands.JedisCommands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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
public class RedisCacheUtils extends CacheUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheUtils.class);

    public static final Long SUC_CODE = 1L;
    public static final String SUC_CODE_STR = "OK";

    public static int defaultTimeoutWarningMillis = 100;

    private static ValueSerializer<String> serializer;

    private static JedisCommands jedis;

    public static void cache(ICache cache) {
        CacheUtils.cache = cache;
        RedisCacheUtils.serializer = ((AbstractCache) cache).getValueSerializer();
        if (cache instanceof ClusterRedisCache) {
            RedisCacheUtils.jedis = ((ClusterRedisCache) cache).getJedisCluster();
        } else if (cache instanceof RedisCache) {
            RedisCacheUtils.jedis = ((RedisCache) cache).getJedisPool().getResource();
        }
    }

    public static <T> T hget(String key, String field) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            String value = jedis.hget(cache.getFullKey(key), field);
            return serializer.unSerialize(value);
        }
    }

    public static <T> T hget(String key, String field, Class<T> clazz) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            String value = jedis.hget(cache.getFullKey(key), field);
            return serializer.unSerialize(value, clazz);
        }
    }

    public static Map<String, Object> hGetAll(String key) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Map<String, String> map = jedis.hgetAll(cache.getFullKey(key));
            if (MapUtils.isEmpty(map)) {
                return new HashMap<>();
            }
            Map<String, Object> realMap = new HashMap<>(map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                realMap.put(entry.getKey(), serializer.unSerialize(entry.getValue()));
            }
            return realMap;
        }
    }

    public static <T> Map<String, T> hGetAll(String key, Class<T> clazz) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Map<String, String> map = jedis.hgetAll(cache.getFullKey(key));
            if (MapUtils.isEmpty(map)) {
                return new HashMap<>();
            }
            Map<String, T> realMap = new HashMap<>(map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                realMap.put(entry.getKey(), serializer.unSerialize(entry.getValue(), clazz));
            }
            return realMap;
        }
    }

    private static final Long HSET_ADD = 1L;
    private static final Long HSET_UPDATE = 0L;

    public static void hset(String key, String field, Object value) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Long rs = jedis.hset(cache.getFullKey(key), field, serializer.serialize(value));
            if (!HSET_ADD.equals(rs) && !HSET_UPDATE.equals(rs)) {
                String message = String.format("Hset failed code %s by key %s value %s", rs, key, value);
                log.error(message);
                throw new CacheRuntimeException(message);
            }
        }
    }

    public static boolean hsetnx(String key, String field, Object value) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Long ok = jedis.hsetnx(cache.getFullKey(key), field, serializer.serialize(value));
            return SUC_CODE.equals(ok);
        }
    }

    public static void hset(String key, Map<String, Object> valueMap) {
        if (MapUtils.isEmpty(valueMap)) {
            return;
        }
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Map<String, String> strValueMap = new HashMap<>(valueMap.size());
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                strValueMap.put(entry.getKey(), serializer.serialize(entry.getValue()));
            }
            String rs = jedis.hmset(cache.getFullKey(key), strValueMap);
            if (!SUC_CODE_STR.equals(rs)) {
                log.error("hset failed {} by key {} map {}", rs, key
                        , JSON.toJSONString(valueMap));
                throw new CacheRuntimeException("hset failed");
            }
        }
    }

    public static <BEAN> void hset(String key, BEAN bean) {
        Map<String, Object> map = MethodUtils.invokeAllGetterMethod(bean);
        hset(key, map);
    }
    public static void hdelAll(String key) {
        delete(key);
    }
    public static void hdel(String key, String... fields) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            Long size = Long.valueOf(fields.length);
            Long rs = jedis.hdel(cache.getFullKey(key), fields);
            if (!size.equals(rs)) {
                log.warn("Delete failed number {} by key {} fields {}", rs, key, JSON.toJSONString(fields));
            }
        }
    }
    public static long hincr(String key, String field, long value) {
        try (AutoTimeLog ignore = AutoTimeLog.warn(defaultTimeoutWarningMillis)) {
            return jedis.hincrBy(cache.getFullKey(key), field, value);
        }
    }
}
