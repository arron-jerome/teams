package com.disney.teams.model.pagination;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 带数据的分页工具类
 * @param <T> 数据类型
 */
@XmlRootElement(name = "PageList")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PageList<T> implements IPageList<T> {

    private static final long serialVersionUID = -4168124165860775460L;
    
    private List<T> content;
    
    @XmlTransient
    private Pager pager;
    
    public PageList() {
        this(new Pager(), new ArrayList<T>());
    }

    public PageList(long totalRecordCount) {
        this();
        pager.setTotalRecordCount(totalRecordCount);
    }

    public PageList(int pageNo, long totalRecordCount) {
        this(totalRecordCount);
        pager.setPageNo(pageNo);
    }

    public PageList(int pageNo, long totalRecordCount, int pageSize) {
        this(pageNo, totalRecordCount);
        pager.setPageSize(pageSize);
    }
    
    public PageList(Pager pager){
        this(pager, new ArrayList<T>());
    }
    
    public PageList(Pager pager, List<T> content){
        this.pager = pager;
        this.content = content;
    }

    @XmlElement
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setPageNo(int pageNo) {
        pager.setPageNo(pageNo);
    }

    @XmlElement
    public int getPageNo() {
        return pager.getPageNo();
    }

    public void setPageSize(int pageSize) {
        pager.setPageSize(pageSize);
    }

    @XmlElement
    public int getPageSize() {
        return pager.getPageSize();
    }

    public void setTotalRecordCount(long totalRecordCount) {
        pager.setTotalRecordCount(totalRecordCount);
    }

    @XmlElement
    public long getTotalRecordCount() {
        return pager.getTotalRecordCount();
    }

    @XmlElement
    public int getPageCount() {
        return pager.getPageCount();
    }

    @XmlElement
    public boolean isFirstPage() {
        return pager.isFirstPage();
    }

    @XmlElement
    public boolean isLastPage() {
        return pager.isLastPage();
    }

    @XmlElement
    public boolean hasNextPage() {
        return pager.hasNextPage();
    }

    @XmlElement
    public boolean hasPreviousPage() {
        return pager.hasPreviousPage();
    }

    @XmlElement
    public int getCurrentPageIndex() {
        return pager.getCurrentPageIndex();
    }

    @XmlElement
    public int getCurrentPageRecordCount() {
        return pager.getCurrentPageRecordCount();
    }

    @XmlElement
    public long getFirstIndexOfCurrentPage() {
        return pager.getFirstIndexOfCurrentPage();
    }

    @XmlElement
    public long getLastIndexOfCurrentPage() {
        return pager.getLastIndexOfCurrentPage();
    }

    @XmlElement
    public long getFirstIndexOfPreviousPage() {
        return pager.getFirstIndexOfPreviousPage();
    }

    @XmlElement
    public long getFirstIndexOfNextPage() {
        return pager.getFirstIndexOfNextPage();
    }

    @XmlElement
    public long getFirstIndexOfPage(int pageNo) {
        return pager.getFirstIndexOfPage(pageNo);
    }

    @XmlElement
    public long getLastIndexOfPage(int pageNo) {
        return pager.getLastIndexOfPage(pageNo);
    }

    public void setPager(Pager pager){
        this.pager = pager;
    }

    public static <Q> boolean isEmpty(IPageList<Q> pagedList){
        return pagedList == null ? true : pagedList.getTotalRecordCount() < 1;
    }

    public static <Q> boolean isNotEmpty(IPageList<Q> pagedList){
        return !isEmpty(pagedList);
    }
}
