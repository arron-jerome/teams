package com.disney.teams.cache.impl.local;

import com.disney.teams.cache.bean.LocalCacheData;
import com.disney.teams.cache.impl.AbstractCache;
import com.disney.teams.utils.type.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class LocalCache extends AbstractCache implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(LocalCache.class);

    private Map<String, LocalCacheData> cacheMap = new ConcurrentHashMap<>();

    private File persisFile;

    private boolean started = false;

    private static final int CLEAR_INTEVAL_SECONDS = 30;
    public LocalCache(){
        this(CLEAR_INTEVAL_SECONDS, null);
    }

    public LocalCache(int clearIntervalSeconds, String persistPath){
        if(clearIntervalSeconds < 0) {
            clearIntervalSeconds = CLEAR_INTEVAL_SECONDS;
        }
        if(persistPath != null) {
            this.persisFile = new File(persistPath);
            if(this.persisFile.isDirectory()) {
                throw new IllegalArgumentException(persistPath + " is a directory");
            } else if(this.persisFile.exists()) {
                readCacheFromFile(persisFile);
            } else {
                FileUtils.mkdirs(persisFile);
            }
        }
        Thread thread = new Thread(new ClearTask(clearIntervalSeconds));
        thread.setName("local-cache-persist-task");
        thread.start();
    }

    private void readCacheFromFile(File file){
        log.info("Read cache from file {}", file);
        try {
            cacheMap = SerialUtils.readObject(file);
            if(cacheMap == null) {
                cacheMap = new ConcurrentHashMap<>();
            }
        } catch (Exception e) {
            log.error(String.format("Ignore cache %s error -> %s", file, e.getMessage()), e);
        }
    }

    private class ClearTask implements Runnable {
        int clearIntevalSeconds;

        ClearTask(int clearIntevalSeconds) {
            this.clearIntevalSeconds = clearIntevalSeconds;
        }

        @Override
        public void run() {
            final byte[] lock = new byte[0];
            final long waitMillis = clearIntevalSeconds * 1000;
            started = true;
            while(started) {
                synchronized (lock) {
                    try {
                        lock.wait(waitMillis);
                        clearData();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                        started = false;
                    } catch (Exception e) {
                        log.error(String.format("clearData caught error --> %s", e.getMessage()), e);
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {
        started = false;
        if(persisFile == null) {
            return;
        }
        try {
            clearData();
        } catch (Exception e) {
            log.warn(String.format("clearData caught error --> %s", e.getMessage()), e);
        }

        try {
            SerialUtils.writeObject(cacheMap, persisFile);
            log.info("Persist cache data to file {}", persisFile);
        } catch (Exception e) {
            log.error(String.format("Persist cache data to file %s error --> %s", persisFile, e.getMessage()), e);
        }
    }

    private synchronized void clearData() {
        List<String> clearKeys = new ArrayList<>();
        for(Map.Entry<String, LocalCacheData> entry : cacheMap.entrySet()) {
            LocalCacheData data = entry.getValue();
            if(data == null) {
                continue;
            }
            if(data.timeout()) {
                clearKeys.add(entry.getKey());
            }
        }
        for(String key : clearKeys) {
            cacheMap.remove(key);
        }
        log.info("Clear expired data size {}", clearKeys.size());
    }

    @Override
    public boolean exists(String key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public <T> T get(String key) {
        LocalCacheData cd = cacheMap.get(key);
        if(cd == null){
            return null;
        }
        if(cd.timeout()){
            return null;
        }
        return cd.data();
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        Object obj = get(key);
        if(obj == null){
            return null;
        } else if (clz.isAssignableFrom(obj.getClass())){
            return (T) obj;
        } else {
            return ClassUtils.convert(obj, clz);
        }
    }

    @Override
    public <T> boolean add(String key, T value) {
        return this.add(key, value, 0);
    }

    @Override
    public <T> boolean add(String key, T value, int expiredSeconds) {
        synchronized (cacheMap){
            LocalCacheData data = cacheMap.get(key);
            if(data != null && !data.timeout()){
                return false;
            }
            data = new LocalCacheData(value, expiredSeconds);
            cacheMap.put(key, data);
        }
        return true;
    }

    @Override
    public <T> boolean put(String key, T value) {
        return put(key, value, 0);
    }

    @Override
    public <T> boolean put(String key, T value, int expiredSeconds) {
        if(value == null){
            return true;
        }
        LocalCacheData cd = new LocalCacheData(value, expiredSeconds);
        cacheMap.put(key, cd);
        return true;
    }

    @Override
    public boolean delete(String key) {
        cacheMap.remove(key);
        return true;
    }

    @Override
    public long incr(String key, long value) {
        LocalCacheData cd;
        synchronized (cacheMap){
            cd = cacheMap.get(key);
            if(cd == null){
                put(key, value);
                return value;
            }
        }
        return cd.incre(value);
    }

    @Override
    public void expire(String key, int expiredSeconds) {
        LocalCacheData cd = cacheMap.get(key);
        if(cd == null){
            return;
        }
        cd.timeout(expiredSeconds);
    }

    @Override
    public int ttl(String key) {
        LocalCacheData cd = cacheMap.get(key);
        if(cd == null){
            return CACHE_NOT_EXISTS;
        }
        if(cd .getExpiredTime() < 1) {
            return CACHE_NO_EXPIRE;
        }
        long ttl = (cd.getExpiredTime() - System.currentTimeMillis()) / 1000;
        return ttl < 0 ? 0 : (int) ttl;
    }
}
