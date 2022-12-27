package com.disney.teams.cache.impl;

import com.disney.teams.cache.ICache;
import com.disney.teams.cache.anno.Cache;
import com.disney.teams.utils.type.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

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
public class CacheAop {

    private static final Logger log = LoggerFactory.getLogger(CacheAop.class);

    private ICache cache;

    /**
     * 根据类、方法及参数创建缓存key值的尾部
     */
    private String key(Cache cache, Object[] params) {
        String key = cache.key();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return key + StringUtils.toString(params, '-');
    }

    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (cache == null) {
            log.warn("There is no cache configure");
            return point.proceed();
        }
        Signature sig = point.getSignature();
        if (!(sig instanceof MethodSignature)) {
            return point.proceed();
        }
        MethodSignature ms = (MethodSignature) sig;
        Method method = ms.getMethod();
        Cache cacheAnno = method.getAnnotation(Cache.class);
        if (cacheAnno == null) {
            return point.proceed();
        }
        String key = key(cacheAnno, point.getArgs());
        Object data = cache.get(key);
        if (data == null) {
            data = point.proceed();
            boolean ok = cache.put(key, data, cacheAnno.timeout());
            if (!ok) {
                log.warn("Put value to cache failed by [{}, {}]", key, data);
            }
        }
        return data;
    }

    public ICache getCache() {
        return cache;
    }

    public void setCache(ICache cache) {
        this.cache = cache;
    }
}
