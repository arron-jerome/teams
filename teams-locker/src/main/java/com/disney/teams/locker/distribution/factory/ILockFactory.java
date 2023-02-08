package com.disney.teams.locker.distribution.factory;

import com.disney.teams.locker.distribution.common.AutoUnlock;
import com.disney.teams.locker.distribution.IDisLock;

import java.util.concurrent.TimeUnit;

public interface ILockFactory{

    void lock(String id, int seconds) throws InterruptedException;

    void lock(String id, long time, TimeUnit unit) throws InterruptedException;

    AutoUnlock autoLock(String id, int seconds) throws InterruptedException;

    AutoUnlock autoLock(String id, long time, TimeUnit unit) throws InterruptedException;

    void unlock(String id);
}
