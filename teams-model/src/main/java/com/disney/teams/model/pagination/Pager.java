package com.disney.teams.model.pagination;

/**
 * @description 分页工具类
 */
public class Pager implements Pagination {

    private static final long serialVersionUID = 9026902366927766568L;

    /**
     * 默认页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    private int pageNo; // 当前页面
    private int pageSize = DEFAULT_PAGE_SIZE; // 每页的记录数，此处北温带了一个初始值，每页显示3条
    private long totalRecordCount; // 总的记录数
    private int totalPage; // 总的页数，由总的记录数和每页的记录数得到，totalSize、pageSize

    public Pager() {
    }

    public Pager(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }


    // 构造函数，传递当前页、总的记录数
    public Pager(int pageNo, int pageSize, long totalRecordCount) {
        this.pageSize = pageSize;
        this.totalRecordCount = totalRecordCount;
        setPageNo(pageNo);
    }


    public void resetPageNo() {
        int totalPage = getPageCount();
        if (pageNo > totalPage) {
            this.pageNo = totalPage;
        }
    }


    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        resetPageNo();
    }

    @Override
    public int getPageNo() {
        return pageNo;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        resetPageNo();
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
//        resetPageNo();
    }

    @Override
    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    @Override
    public int getPageCount() {
        // 所有的页数可以通过总的记录数目求得
        if (pageSize == 0 || totalRecordCount == 0) {
            return 0;
        }
        totalPage = (int) ((totalRecordCount - 1) / pageSize) + 1;
        return totalPage;
    }

    @Override
    public boolean isFirstPage() {
        // 当前页等于1，说明已经是首页了
        return pageNo <= 1;
    }

    @Override
    public boolean isLastPage() {
        // 当前页等于最后页，说明已经是最后一页
        return pageNo >= getPageCount();
    }

    @Override
    public boolean hasNextPage() {
        // 不是最后一页，说明有下一页
        return !isLastPage();
    }

    @Override
    public boolean hasPreviousPage() {
        // 不是第一页，说明有上一页
        return !isFirstPage();
    }

    @Override
    public int getCurrentPageIndex() {
        return pageNo > 0 ? pageNo - 1 : pageNo;
    }

    @Override
    public int getCurrentPageRecordCount() {
        if (pageSize <= 0) {
            return pageSize;
        }
        if (isLastPage()) {
            return (int) (totalRecordCount % pageSize);
        } else {
            return pageSize;
        }
    }

    @Override
    public long getFirstIndexOfCurrentPage() {
        return this.pageNo < 1 ? 0 : (this.pageNo - 1) * this.pageSize + 1;
    }


    @Override
    public long getLastIndexOfCurrentPage() {
        if (isLastPage()) {
            return totalRecordCount;
        } else {
            return getFirstIndexOfCurrentPage() + pageSize - 1;
        }
    }


    @Override
    public long getFirstIndexOfPreviousPage() {
        long index = getFirstIndexOfCurrentPage() - (isFirstPage() ? 0 : pageSize);
        return index < 0 ? 0 : index;
    }


    @Override
    public long getFirstIndexOfNextPage() {
        long index = getFirstIndexOfCurrentPage() + (isLastPage() ? 0 : pageSize);
        return index > totalRecordCount ? totalRecordCount : index;
    }


    @Override
    public long getFirstIndexOfPage(int pageNo) {
        if (pageNo > totalPage) {
            return totalRecordCount;
        } else {
            return (pageNo - 1) * this.pageSize + 1;
        }
    }


    @Override
    public long getLastIndexOfPage(int pageNo) {
        if (pageNo > totalPage) {
            return totalRecordCount;
        } else {
            long index = getFirstIndexOfPage(pageNo) + pageSize;
            return index > totalRecordCount ? totalRecordCount : index;
        }
    }

    public boolean equals(Pager pager) {
        if (pager == null) return false;
        if (pager == this) return true;
        if (this.totalRecordCount != pager.totalRecordCount) return false;
        if (this.pageNo != pager.pageNo) return false;
        if (this.pageSize != pager.pageSize) return false;
        return true;
    }

    /**
     * 获取分页的开始位置（pageSize有默认值）
     */
    public long firstIndexOfCurrentPage() {
        return this.pageNo < 1 ? 0 : (this.pageNo - 1) * (this.pageSize < 1 ? DEFAULT_PAGE_SIZE : this.pageSize);
    }

    /**
     * 获取分页的结束位置(不计算总页数)
     */
    public long lastIndexOfCurrentPage() {
        return firstIndexOfCurrentPage() + (this.pageSize < 1 ? DEFAULT_PAGE_SIZE : this.pageSize) - 1;
    }
}
