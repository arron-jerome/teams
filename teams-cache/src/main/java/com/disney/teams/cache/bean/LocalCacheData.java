package com.disney.teams.cache.bean;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/20
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/20       arron.zhou      1.0.0          create
 */
public class LocalCacheData implements Serializable {

    private static final long serialVersionUID = 4450342550751921308L;

    private long expiredTime;

    private Object data;

    public LocalCacheData() {
    }

    public LocalCacheData(Object data, int timeoutSeconds) {
        this.expiredTime = timeoutSeconds < 1 ? 0 : System.currentTimeMillis() + timeoutSeconds * 1000;
        this.data = data;
    }

    public long incr(long value) {
        if (data instanceof AtomicLong) {
            return ((AtomicLong) data).addAndGet(value);
        } else if (data instanceof Number) {
            synchronized (this) {
                if (data instanceof AtomicLong) {
                    return ((AtomicLong) data).addAndGet(value);
                }
                long now = ((Number) data).longValue() + value;
                data = new AtomicLong(now);
                return now;
            }
        } else {
            throw new UnsupportedOperationException("Only support long value");
        }
    }

    public void timeout(int timeout) {
        this.expiredTime = timeout < 1 ? 0 : System.currentTimeMillis() + timeout * 1000;
    }

    public long decr(long value) {
        return incr(-value);
    }

    public boolean timeout() {
        return expiredTime < 1 ? false : (System.currentTimeMillis() > expiredTime);
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T data() {
        return (T) data;
    }

    public static boolean timeout(LocalCacheData data) {
        return data == null ? true : data.timeout();
    }

}
