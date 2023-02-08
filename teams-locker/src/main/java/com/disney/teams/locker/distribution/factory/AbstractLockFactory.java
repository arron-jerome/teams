package com.disney.teams.locker.distribution.factory;

import com.disney.teams.locker.distribution.IDisLock;
import com.disney.teams.locker.distribution.common.AutoUnlock;
import com.disney.teams.locker.distribution.impl.ZkReentrantLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author arron.zhou
 * @email arron.zhou@disney.com
 * @date 2023/2/8
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2023/2/8      arron.zhou              create
 */
public abstract class AbstractLockFactory implements ILockFactory {

    private final ThreadLocal<Map<String, IDisLock>> lockMapCache = new ThreadLocal<>();

    protected boolean initialized;

    private boolean useUtils = true;

    public boolean isUseUtils() {
        return useUtils;
    }

    public void setUseUtils(boolean useUtils) {
        this.useUtils = useUtils;
    }

    protected abstract IDisLock createLock(String id);

    public abstract void init();

    private IDisLock getLock(String id) {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            }
        }
        IDisLock lock;
        Map<String, IDisLock> lockMap = lockMapCache.get();
        if (lockMap == null) {
            lockMap = new HashMap<>(1);
            lockMapCache.set(lockMap);
            lock = createLock(id);
            lockMap.put(id, lock);
        } else {
            lock = lockMap.get(id);
            if (lock == null) {
                lock = createLock(id);
                lockMap.put(id, lock);
            }
        }
        return lock;
    }

    private IDisLock removeLock(String id) {
        Map<String, IDisLock> lockMap = lockMapCache.get();
        if (lockMap == null) {
            return null;
        }
        IDisLock lock = lockMap.remove(id);
        if (lockMap.isEmpty()) {
            lockMapCache.remove();
        }
        return lock;
    }

    @Override
    public void lock(String id, int seconds) throws InterruptedException {
        lock(id, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String id, long time, TimeUnit unit) throws InterruptedException {
        IDisLock lock = getLock(id);
        lock.lock(time, unit);
    }

    @Override
    public AutoUnlock autoLock(String id, long time, TimeUnit unit) throws InterruptedException {
        lock(id, time, unit);
        return new AutoUnlock(this, id);
    }

    @Override
    public AutoUnlock autoLock(String id, int seconds) throws InterruptedException {
        return autoLock(id, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String id) {
        IDisLock lock = removeLock(id);
        if (lock != null) {
            lock.unlock();
        }
    }

}
