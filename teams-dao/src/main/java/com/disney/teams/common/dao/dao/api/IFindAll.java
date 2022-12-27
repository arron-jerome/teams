package com.disney.teams.common.dao.dao.api;


import com.disney.teams.common.dao.criteria.GenericCriteria;
import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @description 查找所有接口
 * @param <ID>
 * @param <PO>
 */
public interface IFindAll<ID extends Serializable, PO extends GenericEntity<ID>>
{
    public List<PO> findAll();
    public List<PO> findAllByCriteria(GenericCriteria<PO> criteria);
    public List<PO> findAllByPropertyAndValue(String key, Object value);

    public List<PO> findAllBySql(String sql);
    public List<PO> findAllBySql(String sql, Object value);
    public List<PO> findAllBySql(String sql, Object[] values);
    public List<PO> findAllBySql(String sql, Collection<?> valueList);
    
    public <T> List<T> findAllBySql(String sql, Class<T> clazz);
    public <T> List<T> findAllBySql(String sql, Object value, Class<T> clazz);
    public <T> List<T> findAllBySql(String sql, Object[] values, Class<T> clazz);
    public <T> List<T> findAllBySql(String sql, Collection<?> valueList, Class<T> clazz);
    
    
}
