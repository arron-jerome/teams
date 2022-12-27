package com.disney.teams.cache.impl;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Ignore
public class AbstractRedisCacheTest extends AbstractCacheTest {

    public static final String TEST = "test";
    
    private static class Task implements Runnable {
        int i;
        Map<String, Long> increMap;
        Map<String, Long> decreMap;
        public Task(int i, Map<String, Long> increMap, Map<String, Long> decreMap) {
            this.i = i;
            this.increMap = increMap;
            this.decreMap = decreMap;
        }
        @Override
        public void run() {
            boolean negative = (i % 2 == 0);
            String flag = negative ? "-" : "";
            RedisCacheUtils.hincre(TEST, negative ? decreMap : increMap);
            System.out.printf("Test a %s1, b %s2, %s count %s\n", flag, flag, (negative ? "decre" : "incre"),  i);
        }
    }

    @Test
    public void multiIncre() throws InterruptedException {
        Map<String, Long> increMap = new HashMap<>();
        increMap.put("a", 1L);
        increMap.put("b", 2L);
        increMap = Collections.unmodifiableMap(increMap);

        RedisCacheUtils.hdel(TEST, increMap.keySet().toArray(new String[0]));
        RedisCacheUtils.hincre(TEST, increMap);

//        final int INCRE_COUNT = 300000;
        final int COUNT = 300;
//        final AtomicInteger increCount = new AtomicInteger();
//        final AtomicInteger decreCount = new AtomicInteger();

        final Map<String, Long> hincreMap = new HashMap<>();
        hincreMap.put("a", 1L);
        hincreMap.put("b", 2L);
        hincreMap.put("c", 3L);
        final Map<String, Long> hdecreMap = new HashMap<>();
        hdecreMap.put("a", -1L);
        hdecreMap.put("b", -2L);
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for(int i = 0; i < COUNT; ++i) {
            executorService.execute(new Task(i, hincreMap, hdecreMap));
        }

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        Map<String, Long> resultMap = RedisCacheUtils.hgetAll(TEST, Long.class);
        for(Map.Entry<String, Long> entry : increMap.entrySet()) {
            Assert.assertEquals(entry.getValue(), resultMap.get(entry.getKey()));
        }
    }

    @Test
    public void add() throws InterruptedException {
        final String key = "test-add";
        Runnable add = () -> cache.add(key, key, 2);
        Runnable delete  = () -> cache.delete(key);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(delete);
        executorService.execute(add);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        if(cache.exists(key)) {
            System.out.println("Key " + key + " is exist!");
            ThreadUtils.sleep(3000);
            if(cache.exists(key)) {
                Assert.fail();
            }
        }

        int count = 3;
        cache.add(key, key, 4);
        while(--count > -1) {
            cache.add(key, key, 4);
            ThreadUtils.sleep(1000);
        }
        ThreadUtils.sleep(1500);
        if(cache.exists(key)) {
            Assert.fail();
        }
    }
}
