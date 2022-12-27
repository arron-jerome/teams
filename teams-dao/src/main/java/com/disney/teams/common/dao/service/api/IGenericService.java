package com.disney.teams.common.dao.service.api;


import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;

public interface IGenericService<ID extends Serializable
        , PO extends GenericEntity<ID>> extends IDeleteService<ID, PO>, IFindService<ID, PO>, IFindAllService<ID, PO> {
    String NOT_IMPL = "Not implement!";
}

