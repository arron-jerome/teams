package com.disney.teams.locker.distribution.factory;

import com.disney.teams.locker.distribution.DisLockUtils;
import com.disney.teams.locker.distribution.common.AutoUnlock;
import com.disney.teams.locker.distribution.common.DefaultZkCleaner;
import com.disney.teams.locker.distribution.common.ZkCleaner;
import com.disney.teams.locker.distribution.IDisLock;
import com.disney.teams.locker.distribution.impl.ZkReentrantLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ZkLockFactory extends AbstractLockFactory {

    private CuratorFramework client;

    private String zkPath = "/lock/";

    private ZkCleaner zkCleaner;

    private String zkServers;

    private int sessionTimeoutMs = 30000;

    private int connectionTimeoutMs = 30000;

    private int retryMs = 1000;

    private int retryCount = 3;

    public ZkLockFactory() {

    }

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(retryMs, retryCount);
        this.client = CuratorFrameworkFactory.newClient(zkServers, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        this.client.start();
        if (isUseUtils())
            DisLockUtils.lockFactory(this);
    }

    @Override
    protected IDisLock createLock(String id) {
        return new ZkReentrantLock(client, zkPath + id, getZkCleaner());
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public ZkCleaner getZkCleaner() {
        if (zkCleaner == null) {
            synchronized (this) {
                if (zkCleaner == null) {
                    zkCleaner = new DefaultZkCleaner(client);
                }
            }
        }
        return zkCleaner;
    }

    public void setZkCleaner(ZkCleaner zkCleaner) {
        this.zkCleaner = zkCleaner;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getRetryMs() {
        return retryMs;
    }

    public void setRetryMs(int retryMs) {
        this.retryMs = retryMs;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
