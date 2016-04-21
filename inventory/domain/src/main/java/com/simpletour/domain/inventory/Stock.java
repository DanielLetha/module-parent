package com.simpletour.domain.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件描述：库存实体类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016-4-19
 * 备注说明：将库存价格字段与库存主表进行拆分，库存类型仅保持车次与元素两种类型
 * @since 2.0-SNAPSHOT
 */
@Entity
@Table(name = "INV_STOCK")
public class Stock extends StockBound {
    /**
     * 缓存字段：库存主体名称
     * added by shiguanglu at 2015-12-15 18:12
     */
    @Transient
    private String inventoryName;

    /**
     * 缓存字段：库存备注信息
     * added by shiguanglu at 2015-12-22 12:04
     */
    @Transient
    private String inventoryRemark;

    /**
     * 库存数量，只表示day指示的那天的库存数量
     */
    @Column(nullable = false)
    private Integer quantity = 0;

    /**
     * 缓存字段：添加已售库存量
     * added by shiguanglu at 2015-11-27 15:42
     */
    @Transient
    private Integer soldQuantity = 0;

    /**
     * 缓存字段：添加可售库存量
     * added by shiguanglu at 2015-11-27 15:45
     */
    @Transient
    private Integer availableQuantity = 0;

    /**
     * 库存是否上线，默认为上线
     */
    @Column(nullable = false)
    private Boolean online = true;

    public Stock() {
    }

    /**
     * constructor
     * @param inventoryType
     * @param inventoryId
     * @param day
     * @param quantity
     * @param online
     */
    public Stock(InventoryType inventoryType, Long inventoryId, Date day, Integer quantity, Boolean online) {
        super(inventoryType, inventoryId, day);
        this.quantity = quantity;
        this.online = online;
    }

    /**
     * 提供一个全属性带参构造方法
     * @param inventoryType
     * @param inventoryId
     * @param day
     * @param quantity
     * @param soldQuantity
     * @param availableQuantity
     * @param online
     */
    public Stock(InventoryType inventoryType, Long inventoryId, Date day, Integer quantity, Integer soldQuantity, Integer availableQuantity, Boolean online) {
        super(inventoryType, inventoryId, day);
        this.quantity = quantity;
        this.soldQuantity = soldQuantity;
        this.availableQuantity = availableQuantity;
        this.online = online;
    }

    public Integer getQuantity() {
        return null == quantity ? 0 : quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return null == soldQuantity ? 0 : soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Boolean isOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryRemark() {
        return inventoryRemark;
    }

    public void setInventoryRemark(String inventoryRemark) {
        this.inventoryRemark = inventoryRemark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;

        Stock stock = (Stock) o;

        if (getInventoryType() != stock.getInventoryType()) return false;
        if (inventoryId != null ? !inventoryId.equals(stock.getInventoryId()) : stock.getInventoryId() != null)
            return false;
        if (inventoryName != null ? !inventoryName.equals(stock.getInventoryName()) : stock.getInventoryName() != null)
            return false;
        if (inventoryRemark != null ? !inventoryRemark.equals(stock.getInventoryRemark()) : stock.getInventoryRemark() != null)
            return false;
        if (day != null ? !day.equals(stock.getDay()) : stock.getDay() != null) return false;
        if (quantity != null ? !quantity.equals(stock.getQuantity()) : stock.getQuantity() != null)
            return false;
        if (soldQuantity != null ? !soldQuantity.equals(stock.getSoldQuantity()) : stock.getSoldQuantity() != null)
            return false;
        if (availableQuantity != null ? !availableQuantity.equals(stock.getAvailableQuantity()) : stock.getAvailableQuantity() != null)
            return false;

        return !(online != null ? !online.equals(stock.isOnline()) : stock.isOnline() != null);
    }

    @Override
    public int hashCode() {
        int result = getInventoryType() != null ? getInventoryType().hashCode() : 0;
        result = 31 * result + (inventoryId != null ? inventoryId.hashCode() : 0);
        result = 31 * result + (inventoryName != null ? inventoryName.hashCode() : 0);
        result = 31 * result + (inventoryRemark != null ? inventoryRemark.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (soldQuantity != null ? soldQuantity.hashCode() : 0);
        result = 31 * result + (availableQuantity != null ? availableQuantity.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Stock{" +
                "id=" + id +
                ", inventoryType=" + inventoryType +
                ", inventoryId=" + inventoryId +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventoryRemark='" + inventoryRemark + '\'' +
                ", day=" + sdf.format(day) +
                ", quantity=" + quantity +
                ", soldQuantity=" + soldQuantity +
                ", availableQuantity=" + availableQuantity +
                ", online=" + online +
                '}';
    }
}
