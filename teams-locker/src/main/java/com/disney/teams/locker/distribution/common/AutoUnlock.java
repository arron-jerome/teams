package com.disney.teams.locker.distribution.common;

import com.disney.teams.locker.distribution.factory.ILockFactory;
import com.disney.teams.locker.distribution.IDisLock;
import com.disney.teams.utils.type.StringUtils;

public class AutoUnlock implements AutoCloseable {

    private ILockFactory factory;
    private String lockKey;

    public AutoUnlock(ILockFactory factory, String lockKey) {
        this.factory = factory;
        this.lockKey = lockKey;
    }

    @Override
    public void close() {
        if (factory == null || StringUtils.isBlank(lockKey)) {
            return;
        }
        factory.unlock(lockKey);
    }
}
