package com.simpletour.biz.traveltrans.bo;

import com.simpletour.commons.data.domain.DomainPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 2015/12/4.
 */
public class DomainPageBo<T> implements Serializable {

    private static final long serialVersionUID = 1418873658886884883L;

    private long pageSize;
    private long pageIndex;
    private long pageCount;
    private long domainTotalCount;
    private List<T> domains = new ArrayList<T>();

    public DomainPageBo(){}

    public DomainPageBo(long pageSize, long pageIndex, long pageCount, long domainTotalCount) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.pageCount = pageCount;
        this.domainTotalCount = domainTotalCount;
    }

    public DomainPageBo(DomainPage domainPage){
        this.pageSize=domainPage.getPageSize();
        this.pageIndex=domainPage.getPageIndex();
        this.pageCount=domainPage.getPageCount();
        this.domainTotalCount=domainPage.getDomainTotalCount();
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getDomainTotalCount() {
        return domainTotalCount;
    }

    public void setDomainTotalCount(long domainTotalCount) {
        this.domainTotalCount = domainTotalCount;
    }

    public List<T> getDomains() {
        return domains;
    }

    public void setDomains(List<T> domains) {
        this.domains = domains;
    }
}
