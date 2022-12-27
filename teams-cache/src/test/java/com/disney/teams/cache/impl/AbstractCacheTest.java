package com.disney.teams.cache.impl;


import com.disney.teams.cache.CacheTimes;
import com.disney.teams.cache.ICache;
import com.disney.teams.utils.type.LongUtils;
import com.disney.teams.utils.type.ThreadUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.math.BigInteger;

@Ignore
public abstract class AbstractCacheTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected ICache cache;

    @Test
    public void testStringPut(){
        Object[] objs = {"2", (byte)2, (short)2, 2, 2L, 2f
        //    , 2d, new AtomicInteger(2), BigInteger.valueOf(2)
        };
        Long value = 2L;
        for(Object obj : objs) {
            String addKey = "str-add";
            cache.put(addKey, obj);
            Object val = cache.get(addKey);
            Assert.assertEquals(obj, val);
            Long val1 = cache.get(addKey, Long.class);
            Assert.assertEquals(value, val1);
        }

        Object[] objs2 = { 2d, BigInteger.valueOf(2)};
        for(Object obj : objs2) {
            String addKey = "str-add";
            cache.put(addKey, obj);
            Object val = cache.get(addKey, obj.getClass());
            Assert.assertEquals(obj, val);
            Long val1 = cache.get(addKey, Long.class);
            Assert.assertEquals(value, val1);
        }
    }

    @Test
    public void testAdd(){
        String addKey = "add";
        Long value = 2L;
        cache.add(addKey, value);
        boolean ok = cache.add(addKey, value);
        if(ok){
            Assert.fail();
        }
        Long val = cache.get(addKey);
        Assert.assertEquals(value, val);
    }

    @Test
    public void testAddExpired(){
        String addKey = "addExpired";
        Long value = 2L;
        cache.add(addKey, value, 2);
        boolean ok = cache.add(addKey, value, 2);
        if(ok){
            Assert.fail();
        }
        try {
            Thread.sleep(2110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long v = cache.get(addKey);
        Assert.assertNull(v);
        ok = cache.add(addKey, value, 2);
        if(!ok){
            Assert.fail();
        }
        Long val = cache.get(addKey);
        Assert.assertEquals(value, val);
    }

    @Test
    public void testPut(){
        String putKey = "put";
        Long value = 2L;
        boolean ok = cache.put(putKey, value);
        if(!ok){
            throw new RuntimeException();
        }

        ok = cache.put(putKey, value, 2);
        if(!ok){
            throw new RuntimeException();
        }

        ok = cache.exists(putKey);
        if(!ok){
            Assert.fail();
        }
        Long val = cache.get(putKey);
        Assert.assertEquals(value, val);
        try {
            Thread.sleep(2110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long v = cache.get(putKey);
        Assert.assertNull(v);
    }

    @Test
    public void testGet(){
        String getKey = "get";
        Long value = 2L;
        boolean ok = cache.put(getKey, value);
        if(!ok){
            throw new RuntimeException();
        }
        Long v = cache.get(getKey);
        Assert.assertEquals(value, v);
    }

    @Test
    public void testExpire(){
        String expireKey = "expire";
        Long value = 3L;
        boolean ok = cache.put(expireKey, value);
        if(!ok){
            throw new RuntimeException();
        }
        cache.expire(expireKey, 1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long v = cache.get(expireKey);
        Assert.assertEquals(value, v);
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        v = cache.get(expireKey);
        Assert.assertEquals(null, v);
    }

    @Test
    public void testIncreExpire(){
        String increKey = "incre";
        long key = cache.incr(increKey, 1);
        Assert.assertTrue(key >= 1);
        cache.expire(increKey, 1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long v = cache.get(increKey, Long.class);
        Assert.assertTrue(LongUtils.gte1(v));
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        v = cache.get(increKey);
        Assert.assertEquals(null, v);


        key = cache.incr(increKey, 1);
        Assert.assertTrue(key >= 1);
        cache.expire(increKey, 1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Number q = cache.get(increKey);
        Assert.assertTrue(LongUtils.gte1(q.longValue()));
        q = cache.incr(increKey, 1);
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        q = cache.get(increKey);
        Assert.assertEquals(null, v);

    }


    @Test
    public void testTtl() {
        final String key = "aaa";
        cache.delete(key);

        int ttl = cache.ttl(key);
        Assert.assertEquals(ICache.CACHE_NOT_EXISTS, ttl);

        boolean added = cache.addIfOverTtl(key, 1, null, CacheTimes.FOREVER);
        Assert.assertEquals(true, added);

        ttl = cache.ttl(key);
        Assert.assertEquals(ICache.CACHE_NO_EXPIRE, ttl);

        cache.delete(key);
        int timeout = 60;
        added = cache.addIfOverTtl(key, 1, 60, timeout);
        Assert.assertEquals(true, added);

        added = cache.addIfOverTtl(key, 1, 30, timeout);
        Assert.assertEquals(false, added);

        added = cache.addIfOverTtl(key, 1, 60, timeout);
        Assert.assertEquals(true, added);

        for(int i = 0; i < 10; ++i) {
            ttl = cache.ttl(key);
            Assert.assertEquals(i + " " + ttl, true, i <= 60 - ttl);
            ThreadUtils.sleep(1001);
        }
        cache.delete(key);


    }
}
