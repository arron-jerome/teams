
package com.disney.teams.locker.distribution.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultZkCleaner implements ZkCleaner {

    private static final Logger log = LoggerFactory.getLogger(DefaultZkCleaner.class);

    private CuratorFramework client;

    private ExecutorService executor;

    public DefaultZkCleaner(CuratorFramework client) {
        this.client = client;
        this.executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void clean(final String zkPath) {
        executor.execute(new DefaultCleanTask(client, zkPath));
    }

    private static class DefaultCleanTask implements Runnable{

        CuratorFramework client;
        String zkPath;
        DefaultCleanTask(CuratorFramework client, String zkPath){
            this.client = client;
            this.zkPath = zkPath;
        }

        @Override
        public void run() {
            try {
                List list = client.getChildren().forPath(zkPath);
                if (list == null || list.isEmpty()) {
                    client.delete().forPath(zkPath);
                }
            } catch (KeeperException.NoNodeException e) {
                //nothing
            } catch (KeeperException.NotEmptyException e) {
                //nothing
            } catch (Exception e) {
                log.error("Clean zk path {} error -> {}", zkPath, e.getMessage(), e);//准备删除时,正好有线程创建锁
            }
        }
    }
}
