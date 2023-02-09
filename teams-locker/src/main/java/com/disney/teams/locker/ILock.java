package com.disney.teams.locker;

import java.util.concurrent.TimeUnit;

/**
 * @author arron.zhou
 * @date 2023/2/8
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2023/2/8      arron.zhou              create
 */
public interface ILock {
    /**
     * Acquire the mutex - blocking until it's available. Each call to acquire must be balanced by a call
     * to {@link #unlock()}
     *
     * @throws InterruptedException interruptions
     */
    void lock() throws InterruptedException;

    /**
     * Acquire the mutex - blocks until it's available or the given time expires. Each call to acquire that returns true must be balanced by a call
     * to {@link #unlock()}
     *
     * @param time time to wait
     * @param unit time unit
     * @return true if the mutex was acquired, false if not
     * @throws InterruptedException interruptions
     */
    void lock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Perform one release of the mutex.
     */
    void unlock();
}
