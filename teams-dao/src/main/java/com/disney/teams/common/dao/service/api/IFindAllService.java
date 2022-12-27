package com.disney.teams.common.dao.service.api;


import com.disney.teams.common.dao.criteria.GenericCriteria;
import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 查找所有接口
 *
 * @param <ID>
 * @param <PO>
 */
public interface IFindAllService<ID extends Serializable, PO extends GenericEntity<ID>> {
    public List<PO> findAll();

    public List<PO> findAllByCriteria(GenericCriteria<PO> criteria);

    public List<PO> findAllByPropertyAndValue(String key, Object value);
}
