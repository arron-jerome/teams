package com.disney.teams.common.dao.dao.api;

import com.disney.teams.common.dao.criteria.GenericCriteria;
import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @description 删除接口
 * @param <ID>
 * @param <PO>
 */
public interface IDeleteDao<ID extends Serializable, PO extends GenericEntity<ID>> {
    public int deleteAll();

    public int delete(PO po);

    public int delete(PO[] pos);

    public int delete(Collection<PO> pos);

    public int deleteById(ID id);

    public int deleteByIds(ID[] id);

    public int deleteByIds(Collection<ID> ids);

    public int deleteByPropertyAndValue(String key, Object value);

    public int deleteByPropertyValueMap(Map<String, Object> valueMap);

    public int deleteByCriteria(GenericCriteria<PO> criteria);
}
