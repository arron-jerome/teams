package com.disney.teams.cache.impl.redis;

import com.disney.teams.cache.factory.AbstractCacheFactory;
import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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
public abstract class AbstractRedisCacheFactory extends AbstractCacheFactory {

    protected String servers;
    protected int maxTotal;
    protected int maxIdle;
    private int minIdle = 2;
    protected long maxWaitMillis;
    protected boolean testOnBorrow;
    protected boolean testOnReturn;
    private int connectionTimeout;
    private int soTimeout;
    private String password;

    protected boolean defaultRedis = true;

    protected JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWait(Duration.ofMillis(maxWaitMillis));
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        return config;
    }

    protected ConnectionPoolConfig buildConnectConfig() {
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWait(Duration.ofMillis(maxWaitMillis));
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        return config;
    }

    /**
     * 构建集群HostAndPort配置
     */
    protected Set<HostAndPort> buildHostAndPorts(String hosts) {
        Set<HostAndPort> jedisShardInfoList = new HashSet<HostAndPort>();
        String[] hostArrays = hosts.split(",");
        for (String hostStr : hostArrays) {
            String[] ipAndPort = hostStr.split(":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            HostAndPort jedisShardInfo = new HostAndPort(ip, port);
            jedisShardInfoList.add(jedisShardInfo);
        }
        return jedisShardInfoList;
    }
    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDefaultRedis() {
        return defaultRedis;
    }

    public void setDefaultRedis(boolean defaultRedis) {
        this.defaultRedis = defaultRedis;
    }
}
