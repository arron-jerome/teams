package com.disney.teams.common.dao.dao;

import com.disney.teams.common.dao.dao.api.IGenericDao;
import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;

/**
 * DAOFactory AbstractDaoFactory ElasticSearchDaoFactory MongoDaoFactory HibernateDaoFactory
 */
public interface DAOFactory {
    <ID extends Serializable, PO extends GenericEntity<ID>> IGenericDao<ID, PO> getDao(Class<PO> poClass);

    <ID extends Serializable, PO extends GenericEntity<ID>> IGenericDao<ID, PO> getDaoByClassName(String poClassName);

}
