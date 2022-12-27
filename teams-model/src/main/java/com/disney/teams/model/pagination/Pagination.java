package com.disney.teams.model.pagination;

import java.io.Serializable;

/**
 * @description 分页接口
 */
public interface Pagination extends Serializable {
    
    public boolean isFirstPage();

    public boolean isLastPage();

    public boolean hasPreviousPage();

    public boolean hasNextPage();

    public int getCurrentPageIndex();

    public int getPageNo();

    public int getPageCount();

    public int getPageSize();

    public int getCurrentPageRecordCount();

    public long getTotalRecordCount();

    public long getFirstIndexOfCurrentPage();

    public long getLastIndexOfCurrentPage();

    public long getFirstIndexOfPreviousPage();

    public long getFirstIndexOfNextPage();

    public long getFirstIndexOfPage(int pageNo);

    public long getLastIndexOfPage(int pageNo);
}
