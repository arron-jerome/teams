package com.disney.teams.utils.timer;

import com.disney.teams.utils.type.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
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
public class TimeCount {

    private long startTime;

    private long lastTime;

    private List<Map<String, Long>> detail;

    public TimeCount() {
        detail = Collections.synchronizedList(new ArrayList<Map<String, Long>>());
    }

    public TimeCount(long startTime) {
        this();
        this.lastTime = this.startTime = startTime;
    }

    public TimeCount start() {
        lastTime = startTime = System.currentTimeMillis();
        return this;
    }

    public long escape(String name) {
        return escape(name, true);
    }

    public Long get(String name) {
        if (detail == null || detail.isEmpty()) {
            return null;
        }
        //反向遍历性能更佳，更符合业务使用场景
        for (int i = detail.size() - 1; i > -1; --i) {
            Map<String, Long> map = detail.get(i);
            Long value = map.get(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public long mark() {
        return lastTime = System.currentTimeMillis();
    }

    public long escape(String name, boolean marked) {
        long cur = System.currentTimeMillis();
        long escape = cur - lastTime;
        if (marked) {
            lastTime = cur;
        }
        detail.add(MapUtils.newHashMap(name, escape));
        return escape;
    }

    public long escapeFromStart() {
        return System.currentTimeMillis() - startTime;
    }

    public long escapeFromStart(String name) {
        long escape = escapeFromStart();
        detail.add(MapUtils.newHashMap(name, escape));
        return escape;
    }

    public long total() {
        return escapeFromStart("total");
    }

    public List<Map<String, Long>> detailAfterTotal() {
        total();
        return detail;
    }

    public TimeCount clone() {
        TimeCount tc = new TimeCount();
        tc.startTime = this.startTime;
        tc.lastTime = this.lastTime;
        tc.detail = new ArrayList<>(this.detail);
        return tc;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public List<Map<String, Long>> getDetail() {
        return detail;
    }

    public void setDetail(List<Map<String, Long>> detail) {
        this.detail = detail;
    }

    public void addDetail(Map<String, Long> detail) {
        if (detail == null) {
            return;
        }
        if (this.detail == null) {
            this.detail = new ArrayList<>();
        }
        this.detail.add(detail);
    }

    public void addAllDetails(List<Map<String, Long>> details) {
        if (details == null) {
            return;
        }
        if (this.detail == null) {
            this.detail = new ArrayList<>();
        }
        this.detail.addAll(details);
    }

}
