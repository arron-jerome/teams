package com.disney.teams.common.dao.dao.api;

import com.disney.teams.common.dao.criteria.GenericCriteria;
import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * @param <PO>
 * @param <ID>
 * @description 查找单条数据接口
 * @warn 在贪婪模式一对多表级联时，不建议使用此方法，级联返回的实体列表中将只有第一条记录
 */
public interface IFindDao<ID extends Serializable, PO extends GenericEntity<ID>> {
    public PO findFirst();

    public PO findByCriteria(GenericCriteria<PO> criteria);

    public PO findByPropertyAndValue(String key, Object value);

    public PO findBySql(String sql);

    public PO findBySql(String sql, Object value);

    public PO findBySql(String sql, Object[] values);

    public PO findBySql(String sql, Collection<?> valueList);

    public <T> T findBySql(String sql, Class<T> clazz);

    public <T> T findBySql(String sql, Object value, Class<T> clazz);

    public <T> T findBySql(String sql, Object[] values, Class<T> clazz);

    public <T> T findBySql(String sql, Collection<?> valueList, Class<T> clazz);
}
