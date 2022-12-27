package com.disney.teams.cache.serializer;

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
public interface ValueSerializer<S> {
    /**
     * 反序列化S为对象T
     */
    <T> T unSerialize(S value);

    <T> T unSerialize(String value, Class<T> clz);
    /**
     * 序列化对象T为S
     */
    <T> S serialize(T value);
}
