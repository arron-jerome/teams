package com.disney.teams.cache.impl.local;

import com.disney.teams.cache.ICache;
import com.disney.teams.cache.factory.AbstractCacheFactory;

import javax.annotation.PreDestroy;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/21
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/21       arron.zhou      1.0.0          create
 */
public class LocalCacheFactory extends AbstractCacheFactory {

    private int clearIntervalSeconds;

    private String persistPath;

    private int defaultExpiredTime;

    private LocalCache cache;

    public LocalCacheFactory() {

    }

    public LocalCacheFactory(boolean isPersist) {
        if (isPersist) {
            this.clearIntervalSeconds = 30;
            this.persistPath = "../data/local.cache";
        }
    }

    @Override
    public ICache getObject() throws Exception {
        if (cache != null) {
            return cache;
        }
        synchronized (this) {
            if (cache != null) {
                return cache;
            }
            cache = new LocalCache(clearIntervalSeconds, persistPath);
            cache.setDefaultExpiredTime(defaultExpiredTime);
            return cache;
        }
    }

    @PreDestroy
    public void destroy() {
        if (cache != null) {
            cache.destroy();
        }
    }

    public int getClearIntervalSeconds() {
        return clearIntervalSeconds;
    }

    public void setClearIntervalSeconds(int clearIntervalSeconds) {
        this.clearIntervalSeconds = clearIntervalSeconds;
    }

    public int getDefaultExpiredTime() {
        return defaultExpiredTime;
    }

    public void setDefaultExpiredTime(int defaultExpiredTime) {
        this.defaultExpiredTime = defaultExpiredTime;
    }

    public String getPersistPath() {
        return persistPath;
    }

    public void setPersistPath(String persistPath) {
        this.persistPath = persistPath;
    }
}
