
package com.yhtframework.wait;

import com.disney.teams.locker.distribution.common.AutoUnlock;
import com.disney.teams.locker.distribution.factory.ILockFactory;
import com.disney.teams.utils.type.ThreadUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ContextConfiguration({"classpath:spring-lock.xml"})
public class LockTest extends AbstractJUnit4SpringContextTests{

    private static final Logger log = LoggerFactory.getLogger(LockTest.class);

    @Autowired
    private ILockFactory lockFactory;


    @Test
    public void lockAndUnlock(){
        final String uuid = "aaa";
        final int size = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        final AtomicInteger count = new AtomicInteger();
        final AtomicInteger total = new AtomicInteger();
        final AtomicInteger errorCount = new AtomicInteger();
        for(int i = 0; i < size; ++i) {
            executorService.execute(() -> {
                log.info("Begin to lock");
                long cur = System.currentTimeMillis();
                try (AutoUnlock ignored  = lockFactory.autoLock(uuid, 10)){
                    log.info("Obtain lock!");
                    if(count.incrementAndGet() > 1) {
                        errorCount.incrementAndGet();
                        log.error("Current count is " + count);
                    }
                    ThreadUtils.sleep(1000);
                    count.decrementAndGet();
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    if(total.incrementAndGet() >=  size) {
                        synchronized (total) {
                            total.notifyAll();
                        }
                    }
                    log.info("Unlocked {}ms!", System.currentTimeMillis() - cur);
                }
            });
        }
        synchronized (total) {
            try {
                total.wait();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        Assert.assertEquals(true, errorCount.get() < 1);
    }

}
