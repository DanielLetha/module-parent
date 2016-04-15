package com.simpletour.domain.inventory.query;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.BaseQuery;

import java.util.Date;

/**
 * 文件描述：库存查询条件参数封装类
 * 创建人员：石广路
 * 创建日期：2015/12/14 14:47
 * 备注说明：null
 */
public class StockQuery extends BaseQuery implements Cloneable {
    // 库存联合主键
    private StockKey stockKey;

    // 库存查询起始日期
    private Date startDate;

    // 库存查询截止日期
    private Date endDate;

    // 库存上线状态
    private Boolean online;

    public enum InventoryStatus {
        OFFLINE, ONLINE, STOP, NORMAL;

        public Object getValue() {
            if (OFFLINE == this) {
                return false;
            }
            else if (ONLINE == this) {
                return true;
            }
            else if (STOP == this) {
                return "'stop'";
            }
            else if (NORMAL == this) {
                return "'normal'";
            } else {
                return null;
            }
        }
    }

    // 库存托管对象状态，如上下线、正常或停止
    private InventoryStatus inventoryStatus;

    // 库存托管对象名称，如元素库存“九寨沟门票+观光车1张”
    private String inventoryName;

    public StockQuery() {
        super();
    }

    /**
     * 查询某天的库存
     * @param stockKey
     * @param stockDate
     * @param online
     */
    public StockQuery(StockKey stockKey, Date stockDate, Boolean online) {
        this.stockKey = stockKey;
        this.startDate = stockDate;
        this.endDate = stockDate;
        this.online = online;
    }

    /**
     * 查询某个时间段的库存
     * @param stockKey
     * @param startDate
     * @param endDate
     * @param online
     */
    public StockQuery(StockKey stockKey, Date startDate, Date endDate, Boolean online) {
        this.stockKey = stockKey;
        this.startDate = startDate;
        this.endDate = endDate;
        this.online = online;
    }

    /**
     * 根据默认的排序和分页规则来查询某天的库存
     * @param stockKey
     * @param stockDate
     * @param online
     * @param inventoryName
     * @param pageIndex
     */
    public StockQuery(StockKey stockKey, Date stockDate, Boolean online, InventoryStatus inventoryStatus, String inventoryName, int pageIndex) {
        setPageIndex(pageIndex);
        this.stockKey = stockKey;
        this.startDate = stockDate;
        this.endDate = stockDate;
        this.online = online;
        this.inventoryStatus = inventoryStatus;
        this.inventoryName = inventoryName;
    }

    /**
     * 根据默认的排序和分页规则来查询某个时间段的库存
     * @param stockKey
     * @param startDate
     * @param endDate
     * @param online
     * @param inventoryName
     * @param pageIndex
     */
    public StockQuery(StockKey stockKey, Date startDate, Date endDate, Boolean online, InventoryStatus inventoryStatus, String inventoryName, int pageIndex) {
        setPageIndex(pageIndex);
        this.stockKey = stockKey;
        this.startDate = startDate;
        this.endDate = endDate;
        this.online = online;
        this.inventoryStatus = inventoryStatus;
        this.inventoryName = inventoryName;
    }

    /**
     * 根据排序和分页规则来查询某天的库存
     * @param stockKey
     * @param stockDate
     * @param online
     * @param inventoryName
     * @param orderByFiledName
     * @param orderBy
     * @param pageSize
     * @param pageIndex
     */
    public StockQuery(StockKey stockKey, Date stockDate, Boolean online, InventoryStatus inventoryStatus, String inventoryName, String orderByFiledName, IBaseDao.SortBy orderBy, int pageSize, int pageIndex) {
        super(orderByFiledName, orderBy, pageSize, pageIndex);
        this.stockKey = stockKey;
        this.startDate = stockDate;
        this.endDate = stockDate;
        this.online = online;
        this.inventoryStatus = inventoryStatus;
        this.inventoryName = inventoryName;
    }

    /**
     * 根据排序和分页规则来查询某个时间段的库存
     * @param stockKey
     * @param startDate
     * @param endDate
     * @param online
     * @param inventoryName
     * @param orderByFiledName
     * @param orderBy
     * @param pageSize
     * @param pageIndex
     */
    public StockQuery(StockKey stockKey, Date startDate, Date endDate, Boolean online, InventoryStatus inventoryStatus, String inventoryName, String orderByFiledName, IBaseDao.SortBy orderBy, int pageSize, int pageIndex) {
        super(orderByFiledName, orderBy, pageSize, pageIndex);
        this.stockKey = stockKey;
        this.startDate = startDate;
        this.endDate = endDate;
        this.online = online;
        this.inventoryStatus = inventoryStatus;
        this.inventoryName = inventoryName;
    }

    public StockKey getStockKey() {
        return stockKey;
    }

    public void setStockKey(StockKey stockKey) {
        this.stockKey = stockKey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    @Override
    public Object clone() {
        StockQuery stockQuery = null;
        try {
            stockQuery = (StockQuery)super.clone();
        } catch (CloneNotSupportedException exc) {
            exc.printStackTrace();
            return null;
        }

        stockQuery.stockKey.setInventoryType(stockKey.getInventoryType());
        stockQuery.stockKey.setInventoryTypeId(stockKey.getInventoryTypeId());
        stockQuery.startDate = null == startDate ? null : (Date)startDate.clone();
        stockQuery.endDate = null == endDate ? null : (Date)endDate.clone();
        stockQuery.online = online;
        stockQuery.inventoryStatus = inventoryStatus;
        stockQuery.inventoryName = inventoryName;

        stockQuery.setOrderByFiledName(getOrderByFiledName());
        stockQuery.setOrderBy(getOrderBy());
        stockQuery.setPageSize(getPageSize());
        stockQuery.setPageIndex(getPageIndex());

        return stockQuery;
    }
}
