package com.disney.teams.locker.distribution.impl;

import com.disney.teams.locker.distribution.IDisLock;
import com.disney.teams.locker.distribution.common.ZkCleaner;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ZkReentrantLock implements IDisLock {

    private static final Logger log = LoggerFactory.getLogger(ZkReentrantLock.class);

    private CuratorFramework client;

    private String path;

    private InterProcessLock interProcessLock;

    private ZkCleaner zkCleaner;

    public ZkReentrantLock(CuratorFramework client, String lockId) {
        this(client, lockId, null);
    }

    public ZkReentrantLock(CuratorFramework client, String lockId, ZkCleaner zkCleaner) {
        this.client = client;
        this.path = lockId;
        interProcessLock = new InterProcessMutex(client, this.path);
        this.zkCleaner = zkCleaner;
    }


    @Override
    public void lock() throws InterruptedException {
        try {
            interProcessLock.acquire();
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void lock(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            long cur = System.currentTimeMillis();
            boolean acquired = interProcessLock.acquire(timeout, unit);
            if (!acquired) {
                throw new InterruptedException((System.currentTimeMillis() - cur) + "ms timeout");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void unlock() {
        try {
            if (interProcessLock.isAcquiredInThisProcess()) {
                interProcessLock.release();
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        } finally {
            if (zkCleaner != null) {
                zkCleaner.clean(path);
            }
        }
    }

    @Override
    public boolean isAcquiredInThisProcess() {
        return interProcessLock.isAcquiredInThisProcess();
    }
}
