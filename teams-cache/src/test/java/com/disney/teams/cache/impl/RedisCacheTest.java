package com.disney.teams.cache.impl;

import com.alibaba.fastjson.JSONObject;
import com.disney.teams.cache.utils.RedisCacheUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({"classpath:spring-redis.xml"})
public class RedisCacheTest extends AbstractRedisCacheTest {

    @Test
    public void hdelAll() {
        JSONObject value = RedisCacheUtils.hget("front-server"
                , "aliyun-us-east-amazon");
        System.out.println(value);
//        final String key = "htest";
//        final String[] fields = {"a", "b", "c"};
//        for(String field : fields) {
//            RedisCacheUtils.hincre(key, field, 1);
//        }
//        RedisCacheUtils.hset(key, "d", 1);
//        RedisCacheUtils.hset(key, "d", 2);
//        long val = RedisCacheUtils.hget(key, "d", long.class);
//        Assert.assertEquals(2, val);
//
//
//        RedisCacheUtils.hdelAll(key);
//        //        RedisCacheUtils.delete(key);
//        Map<String, Long> map = RedisCacheUtils.hgetAll(key, Long.class);
//        Assert.assertEquals(true, MapUtils.isEmpty(map));
    }

}
