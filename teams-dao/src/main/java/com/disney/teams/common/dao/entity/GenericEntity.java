package com.disney.teams.common.dao.entity;

import java.io.Serializable;

/**
 * 实体超类
 */
public abstract class GenericEntity<ID extends Serializable> implements Cloneable, Serializable{

    private static final long serialVersionUID = -2660961504881346872L;

    public GenericEntity() {}

    public abstract ID getId();
    public abstract void setId(ID id);
    
}
