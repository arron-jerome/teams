package com.disney.teams.common.dao.dao.api;

import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;

/**
 * @param <ID>
 * @param <PO>
 * @description 所有dao层接口
 */
public interface IGenericDao<ID extends Serializable, PO extends GenericEntity<ID>> extends
        IDeleteDao<ID, PO>, IFindDao<ID, PO>, IFindAll<ID, PO> {
    public static final String NOT_IMPL = "Not implement!";
}
