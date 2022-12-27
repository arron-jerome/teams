package com.disney.teams.model.pagination;

import java.io.Serializable;
import java.util.List;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public interface IPageList<T> extends Pagination, Cloneable, Serializable{
    
    List<T> getContent();
    
}
