package com.simpletour.domain.inventory;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 文件描述：库存价格实体类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/19 16:05
 * 备注说明：增加业务订单项编号、销售条目父编号、有效状态和销售时间等字段，移除订单编号字段
 * @since 2.0-SNAPSHOT
 */
@Entity
@Table(name = "INV_SOLD_ENTRY")
public class SoldEntry extends StockBound {
    /**
     * 业务订单项编号
     */
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    /**
     * 该销售条目的父编号
     */
    @Column(name = "pid", nullable = false)
    private Long parentId;

    /**
     * 销售数量
     */
    @Column(nullable = false)
    private Integer quantity = 0;

    /**
     * 销售状态是否有效，默认为有效，订单被取消、关闭时，该字段需要被设置为无效
     */
    @Column(nullable = false)
    private Boolean valid = true;

    /**
     * 销售时间，值为产生销售记录时的当前系统时间戳
     */
    @Column(name = "sold_time", nullable = false)
    protected Long soldTime;

    @Transient
    private List<SoldEntry> soldEntryList;

    public SoldEntry() {
    }

    /**
     * 用以查询库存某天的销售量
     * @param inventoryType
     * @param inventoryId
     * @param day
     * @param valid
     */
    public SoldEntry(InventoryType inventoryType, Long inventoryId, Date day, Boolean valid) {
        super(inventoryType, inventoryId, day);
        this.valid = null == valid ? true : valid;
    }

    public SoldEntry(InventoryType inventoryType, Long inventoryId, Date day, Long itemId, Long parentId, Integer quantity, Boolean valid, Long soldTime) {
        super(inventoryType, inventoryId, day);
        this.itemId = itemId;
        this.parentId = parentId;
        this.quantity = quantity;
        this.valid = null == valid ? true : valid;
        this.soldTime = soldTime;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Long soldTime) {
        this.soldTime = soldTime;
    }

    public List<SoldEntry> getSoldEntryList() {
        return soldEntryList;
    }

    public void setSoldEntryList(List<SoldEntry> soldEntryList) {
        this.soldEntryList = soldEntryList;
    }
}
