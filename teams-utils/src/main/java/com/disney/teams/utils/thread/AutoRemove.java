package com.disney.teams.utils.thread;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class AutoRemove<T> implements AutoCloseable {

    private final ThreadLocal<T> threadLocal;

    public AutoRemove(ThreadLocal<T> threadLocal) {
        this.threadLocal = threadLocal;
    }

    public T get() {
        return threadLocal == null ? null : threadLocal.get();
    }

    @Override
    public void close() {
        if (threadLocal != null) {
            threadLocal.remove();
        }
    }

    public T closeAndGet() {
        if (threadLocal == null) {
            return null;
        }
        T t = threadLocal.get();
        threadLocal.remove();
        return t;
    }
}
