package com.disney.teams.common.dao.service;

import com.disney.teams.common.dao.entity.GenericEntity;

import java.io.Serializable;

public abstract class CallbackServiceSupport<ID extends Serializable,PO extends GenericEntity<ID>> extends GenericServiceSupport<ID, PO>{

//    protected ISaveCallback<ID, PO> saveCallBack;
//    protected IUpdateCallback<ID, PO> updateCallBack;
//    protected IDeleteCallback<ID, PO> deleteCallBack;

//    @Override
//    public ID save(PO dto) {
//        if(saveCallBack == null){
//            return super.save(dto);
//        }
//
//        Msg rs = saveCallBack.beforeSave(dto);
//        if(Msg.isNotOk(rs)){
//            throw new ServiceRuntimeException(rs);
//        }
//        ID id = dao.save(dto);
//        dto.setId(id);
//        saveCallBack.afterSave(dto);
//        return id;
//    }
}
