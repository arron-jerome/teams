package com.disney.teams.cache.impl.redis;

import com.disney.teams.cache.CacheRuntimeException;
import com.disney.teams.cache.ICache;
import com.disney.teams.cache.utils.RedisCacheUtils;
import com.disney.teams.cache.serializer.FastJsonSerializer;
import com.disney.teams.utils.type.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.*;

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
public class RedisCacheFactory extends AbstractRedisCacheFactory {

    private RedisCache cache;

    @Override
    public ICache getObject() throws Exception {
        if (cache != null) {
            return cache;
        }
        synchronized (this) {
            if (cache != null) {
                return cache;
            }
            Set<HostAndPort> hostAndPort = buildHostAndPorts(servers);
            if (CollectionUtils.isEmpty(hostAndPort)) {
                throw new CacheRuntimeException("servers is empty or with wrong format,use 127.0.0.1:6379");
            }
            HostAndPort next = hostAndPort.iterator().next();
            JedisPoolConfig config = buildPoolConfig();
            JedisPool jedisPool = new JedisPool(config
                    , next.getHost(), next.getPort()
                    , Protocol.DEFAULT_TIMEOUT, getPassword());
            cache = new RedisCache(jedisPool);
            cache.setValueSerializer(new FastJsonSerializer());
            cache.setPrefixKey(serverId);
            if (defaultRedis) {
                RedisCacheUtils.cache(cache);
            }
            return cache;
        }
    }
}
