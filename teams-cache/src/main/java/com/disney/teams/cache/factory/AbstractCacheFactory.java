package com.disney.teams.cache.factory;

import com.disney.teams.cache.ICache;

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
public abstract class AbstractCacheFactory implements ICacheFactory {

    protected String serverId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
