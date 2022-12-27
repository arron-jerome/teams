package com.disney.teams.cache;

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
public interface ICache {

    int CACHE_NO_EXPIRE = -1;

    int CACHE_NOT_EXISTS = -2;

    /**
     * 设置默认过期时间
     */
    void setDefaultExpiredTime(int seconds);

    /**
     * 获取默认过期时间
     */
    int getDefaultExpiredTime();

    /**
     * 设置默认操作超时时间
     */
    void setDefaultTimeOut(int timeout);

    /**
     * 获取默认操作超时时间
     */
    int getDefaultTimeOut();

    /**
     * 设置key默认前缀
     */
    void setPrefixKey(String prefixKey);

    /**
     * 获取key默认前缀
     */
    String getPrefixKey();

    String getFullKey(String key);

    boolean exists(String key);

    /**
     * 从缓存中取值<br />
     * 当存入的数据类型为java.math.BigInteger, BigDecimal, AtomicInteger等Numeric数值类型时，
     * 请使用{@link #get(String, Class)}
     */
    <T extends Object> T get(String key);

    <T extends Object> T get(String key, Class<T> clz);

    /**
     * 如果缓存数据的剩余时间小于ttl，则不返回数据
     */
    <T extends Object> T getIfOverTtl(String key, Integer ttl);

    <T extends Object> T getIfOverTtl(String key, Integer ttl, Class<T> clz);

    /**
     * 新增缓存item
     */
    <T extends Object> boolean put(String key, T value);

    /**
     * 新增缓存item
     */
    <T extends Object> boolean put(String key, T value, int expiredSeconds);

    /**
     * add data with defaultExpiredTime(oneDay),failed if already exist
     *
     * @param key   key
     * @param value value
     */
    <T extends Object> boolean add(String key, T value);

    /**
     * add data,failed if already exist
     *
     * @param key            key
     * @param value          value
     * @param expiredSeconds >0 expired
     *                       <0 forever
     */
    <T extends Object> boolean add(String key, T value, int expiredSeconds);

    /**
     * 如果缓存数据的剩余时间小于ttl，则add为true
     */
    <T extends Object> boolean addIfOverTtl(String key, T value, Integer ttl);

    <T extends Object> boolean addIfOverTtl(String key, T value, Integer ttl, int expiredSeconds);

    /**
     * 删除缓存
     *
     * @param key
     */
    boolean delete(String key);

    /**
     * 删除缓存
     *
     * @param keys
     */
    int delete(String... keys);

    /**
     * 缓存计数加
     *
     * @param key
     * @param value
     */
    long incr(String key, long value);

    /**
     * 缓存计数减
     *
     * @param key
     * @param value
     */
    long decr(String key, long value);

    /**
     * 设置过期时间,适用于redis
     */
    void expire(String key, int expiredSeconds);

    /**
     * 剩余过期时间
     */
    int ttl(String key);
}

