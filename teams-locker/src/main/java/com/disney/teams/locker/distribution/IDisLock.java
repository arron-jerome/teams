package com.disney.teams.locker.distribution;

import com.disney.teams.locker.ILock;

/**
 * @author arron.zhou
 * @date 2023/2/8
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2023/2/8      arron.zhou              create
 */
public interface IDisLock extends ILock {
    /**
     * Returns true if the mutex is acquired by a thread in this JVM
     *
     * @return true/false
     */
    boolean isAcquiredInThisProcess();
}
