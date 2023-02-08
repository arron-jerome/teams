package com.disney.teams.locker.distribution;

import com.disney.teams.exception.BasicRuntimeException;
import com.disney.teams.locker.ILock;
import com.disney.teams.locker.distribution.common.AutoUnlock;
import com.disney.teams.locker.distribution.factory.ILockFactory;

import java.util.concurrent.TimeUnit;

public class DisLockUtils {

    private static ILockFactory lockFactory;

    public static void lockFactory(ILockFactory lockFactory) {
        if (DisLockUtils.lockFactory != null) {
            throw new BasicRuntimeException("lockFactory already exist");
        }
        DisLockUtils.lockFactory = lockFactory;
    }

    public static void lock(String id, int seconds) throws InterruptedException {
        lockFactory.lock(id, seconds);
    }

    public static void lock(String id, long time, TimeUnit unit) throws InterruptedException {
        lockFactory.lock(id, time, unit);
    }

    public static AutoUnlock autoLock(String id, long time, TimeUnit unit) throws InterruptedException {
        return lockFactory.autoLock(id, time, unit);
    }

    public static AutoUnlock autoLock(String id, int seconds) throws InterruptedException {
        return lockFactory.autoLock(id, seconds);
    }

    public static void unlock(String id) {
        lockFactory.unlock(id);
    }
}
