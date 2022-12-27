package com.disney.teams.common.dao.service;

import com.disney.teams.common.dao.criteria.GenericCriteria;
import com.disney.teams.common.dao.dao.api.IGenericDao;
import com.disney.teams.common.dao.entity.GenericEntity;
import com.disney.teams.common.dao.service.api.IGenericService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

public abstract class GenericServiceSupport<ID extends Serializable, PO extends GenericEntity<ID>> implements IGenericService<ID, PO> {

    protected IGenericDao<ID, PO> genericDao;

    @PostConstruct
    public void initDefaultDaoFactory() {
        //AbstractDaoFactory HibernateDaoFactory MongoDaoFactory ElasticSearchDaoFactory
//        IDaoFactory daoFactory = DaoUtils.getDefaultDaoFactory();
//        if (daoFactory != null) {
//            setDaoFactory(daoFactory);
//        }CallbackServiceSupport
    }

//    protected void setDaoFactory(IDaoFactory daoFactory) {
//        if (genericDao == null) {
//            Class<PO> poClass = (Class<PO>) ClassUtils.getGenericType(getClass(), GenericServiceSupport.class, 1);
//            genericDao = daoFactory.getDao(poClass);
//        }
//    }

//    public IGenericDao<ID, PO> getDao() {
//        return genericDao;
//    }
//
//    public void setDao(IGenericDao<ID, PO> dao) {
//        this.dao = dao;
//    }


    /*-------------------------find start--------------------------------*/
    @Override
    public PO findFirst() {
        return genericDao.findFirst();
    }

    @Override
    public PO findByCriteria(GenericCriteria<PO> criteria) {
        return genericDao.findByCriteria(criteria);
    }

    @Override
    public PO findByPropertyAndValue(String key, Object value) {
        return genericDao.findByPropertyAndValue(key, value);
    }

    /**************************find all start***************************/
    @Override
    public List<PO> findAll() {
        return genericDao.findAll();
    }

    @Override
    public List<PO> findAllByCriteria(GenericCriteria<PO> criteria) {
        return genericDao.findAllByCriteria(criteria);
    }

    @Override
    public List<PO> findAllByPropertyAndValue(String key, Object value) {
        return genericDao.findAllByPropertyAndValue(key, value);
    }


    /*-------------------------delete start--------------------------------*/
    @Override
    public int deleteAll() {
        return genericDao.deleteAll();
    }

    @Override
    public int delete(PO po) {
        return genericDao.delete(po);
    }

    @Override
    public int delete(PO[] pos) {
        if (ArrayUtils.isEmpty(pos)) {
            return 0;
        }
        return genericDao.delete(pos);
    }

    @Override
    public int delete(Collection<PO> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return 0;
        }
        return genericDao.delete(pos);
    }

    @Override
    public int deleteById(ID id) {
        return genericDao.deleteById(id);
    }

    @Override
    public int deleteByIds(ID[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return 0;
        }
        return genericDao.deleteByIds(ids);
    }

    @Override
    public int deleteByIds(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        return genericDao.deleteByIds(ids);
    }

    @Override
    public int deleteByPropertyAndValue(String key, Object value) {
        return genericDao.deleteByPropertyAndValue(key, value);
    }

    @Override
    public int deleteByPropertyValueMap(Map<String, Object> valueMap) {
        if (MapUtils.isEmpty(valueMap)) {
            return 0;
        }
        return genericDao.deleteByPropertyValueMap(valueMap);
    }

    @Override
    public int deleteByCriteria(GenericCriteria<PO> criteria) {
        return genericDao.deleteByCriteria(criteria);
    }
}
