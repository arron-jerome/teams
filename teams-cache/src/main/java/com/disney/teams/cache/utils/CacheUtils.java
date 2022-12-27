package com.disney.teams.cache.utils;
import com.disney.teams.cache.ICache;

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
public class CacheUtils {

    protected static ICache cache;

    public void setCache(ICache cache) {
        CacheUtils.cache = cache;
    }

    public static void cache(ICache cache) {
        CacheUtils.cache = cache;
    }

    /**
     * 获取默认过期时间
     *
     * @return
     */
    public static int getDefaultExpiredTime() {
        return cache.getDefaultExpiredTime();
    }

    /**
     * 获取默认超时时间
     *
     * @return
     */
    public static int getDefaultTimeOut() {
        return cache.getDefaultTimeOut();
    }

    /**
     * 获取key默认前缀
     */
    public static String getPrefixKey() {
        return cache.getPrefixKey();
    }

    public static boolean exists(String key) {
        return cache.exists(key);
    }

    public static String getFullKey(String key) {
        return cache.getFullKey(key);
    }

    /**
     * 从缓存中取值<br />
     * 当存入的数据类型为java.math.BigInteger, BigDecimal, AtomicInteger等Numberic数值类型时，
     * 请使用{@link #get(String, Class)}
     *
     * @param key
     * @return
     */
    public static <T> T get(String key) {
        return cache.get(key);
    }

    public static <T> T get(String key, Class<T> clz) {
        return cache.get(key, clz);
    }

    /**
     * 如果缓存数据的剩余时间小于ttl，则不返回数据
     */
    public static <T> T getIfOverTtl(String key, Integer ttl) {
        return cache.getIfOverTtl(key, ttl);
    }

    public static <T> T getIfOverTtl(String key, Integer ttl, Class<T> clz) {
        return cache.getIfOverTtl(key, ttl, clz);
    }

    /**
     * 往缓存里加数据，已经存在则返回false
     *
     * @param key
     * @param value
     * @return
     */
    public static <T> boolean add(String key, T value) {
        return cache.add(key, value);
    }

    public static <T> boolean add(String key, T value, int expiredSeconds) {
        return cache.add(key, value, expiredSeconds);
    }

    /**
     * 如果缓存数据的剩余时间小于ttl，则add为true
     *
     * @param key
     * @param ttl
     * @param <T>
     * @return
     */
    public static <T> boolean addIfOverTtl(String key, T value, Integer ttl) {
        return cache.addIfOverTtl(key, value, ttl);
    }

    public static <T> boolean addIfOverTtl(String key, T value, Integer ttl, int expiredSeconds) {
        return cache.addIfOverTtl(key, value, ttl, expiredSeconds);
    }

    /**
     * 新增缓存item
     *
     * @param key
     * @param value
     * @return
     */
    public static <T> boolean put(String key, T value) {
        return cache.put(key, value);
    }

    public static <T> boolean put(String key, T value, int expiredSeconds) {
        return cache.put(key, value, expiredSeconds);
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public static boolean delete(String key) {
        return cache.delete(key);
    }

    public static int delete(String... keys) {
        return cache.delete(keys);
    }

    /**
     * 缓存计数加
     *
     * @param key
     * @param value
     * @return
     */
    public static long incr(String key, long value) {
        return cache.incr(key, value);
    }

    /**
     * 缓存计数减
     *
     * @param key
     * @param value
     * @return
     */
    public static long decr(String key, long value) {
        return cache.decr(key, value);
    }

    /**
     * 设置过期时间,适用于redis
     *
     * @param key
     * @param expiredSeconds 单位: seconds
     */
    public static void expire(String key, int expiredSeconds) {
        cache.expire(key, expiredSeconds);
    }

    /**
     * 剩余过期时间
     *
     * @param key
     * @return
     */
    public static int ttl(String key) {
        return cache.ttl(key);
    }
}
