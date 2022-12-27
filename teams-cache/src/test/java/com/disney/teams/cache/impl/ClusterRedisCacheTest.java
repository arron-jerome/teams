package com.disney.teams.cache.impl;

import com.disney.teams.cache.utils.RedisCacheUtils;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

@ContextConfiguration({"classpath:spring-redis-cluster.xml"})
public class ClusterRedisCacheTest extends AbstractRedisCacheTest {

    @Test
    public void hdelAll() {
        final String key = "htest";
        final String[] fields = {"a", "b", "c"};
        for (String field : fields) {
            RedisCacheUtils.hincr(key, field, 1);
        }
        RedisCacheUtils.hset(key, "d", 1);
        RedisCacheUtils.hset(key, "d", 2);
        long val = RedisCacheUtils.hget(key, "d", long.class);
        Assert.assertEquals(2, val);


        RedisCacheUtils.hdelAll(key);
//        RedisCacheUtils.delete(key);
        Map<String, Long> map = RedisCacheUtils.hgetAll(key, Long.class);
        Assert.assertEquals(true, MapUtils.isEmpty(map));
    }


}
