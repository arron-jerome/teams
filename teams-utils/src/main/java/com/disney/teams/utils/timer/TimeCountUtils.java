package com.disney.teams.utils.timer;

import com.disney.teams.utils.thread.AutoRemove;

import java.util.List;
import java.util.Map;

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
public abstract class TimeCountUtils {

    private static final ThreadLocal<TimeCount> timeCountThreadLocal = new ThreadLocal<>();

    private static final AutoRemove<TimeCount> AUTO_REMOVE = new AutoRemove<>(timeCountThreadLocal);

    public static final AutoRemove<TimeCount> start() {
        TimeCount tc = new TimeCount();
        timeCountThreadLocal.set(tc.start());
        return AUTO_REMOVE;
    }

    public static final TimeCount get() {
        return timeCountThreadLocal.get();
    }

    public static AutoRemove<TimeCount> set(TimeCount value) {
        timeCountThreadLocal.set(value);
        return AUTO_REMOVE;
    }

    public static TimeCount remove() {
        TimeCount timeCount = timeCountThreadLocal.get();
        timeCountThreadLocal.remove();
        return timeCount;
    }

    public static final long escape(String name) {
        return escape(name, true);
    }

    public static final long escape(String name, boolean marked) {
        TimeCount tc = get();
        if (tc != null) {
            return tc.escape(name, marked);
        }
        return 0L;
    }

    public long escapeFromStart(String name) {
        TimeCount tc = get();
        if (tc != null) {
            return tc.escapeFromStart(name);
        }
        return 0L;
    }

    public long total() {
        return escapeFromStart("total");
    }

    public List<Map<String, Long>> getDetail() {
        TimeCount tc = get();
        if (tc != null) {
            return tc.getDetail();
        }
        return null;
    }
}
