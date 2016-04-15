package com.simpletour.domain.inventory;


import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by igotti on 15-11-19.
 * 销售库存，不需要日志支持。
 */
@Entity
@Table(name = "INV_SOLD_ENTRY")
public class SoldEntry extends BaseDomain {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "pid")
    private Long parentId;  // 注：parendId单词拼写错误，应该是parentId

    /**
     * 库存类型，包括行程、产品、元素、车次
     */
    @Column
    @Enumerated(EnumType.STRING)
    private InventoryType inventoryType;

    /**
     * 库存类型Id，对应行程、产品、元素、车次表（对象）的主键（id）
     */
    @Column
    private Long inventoryTypeId;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date day;

    /**
     * 库存数量，只表示day指示的那天的库存数量
     */
    @Column
    private Integer quantity = 0;

    /**
     * 该销售库存是否有效，订单取消、关闭都将设置该字段为无效
     */
    @Column
    private Boolean status = true;
    
    /**
     * 快照
     */
    @Column(columnDefinition = "text")
    private String snapshot;

    @Transient
    private List<SoldEntry> soldEntryList;

    public SoldEntry() {

    }

    /**
     * 用以查询库存某天的销售量
     * @param inventoryType
     * @param inventoryTypeId
     * @param day
     */
    public SoldEntry(InventoryType inventoryType, Long inventoryTypeId, Date day, Boolean status) {
        this.inventoryType = inventoryType;
        this.inventoryTypeId = inventoryTypeId;
        this.day = day;
        if (null != status) {
            this.status = status;
        }
    }

    public SoldEntry(Long orderId, Long itemId, Long parentId, InventoryType inventoryType, Long inventoryTypeId, Date day, Integer quantity, Boolean status, String snapshot) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.parentId = parentId;
        this.inventoryType = inventoryType;
        this.inventoryTypeId = inventoryTypeId;
        this.day = day;
        this.quantity = quantity;
        this.status = status;
        this.snapshot = snapshot;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Long getInventoryTypeId() {
        return inventoryTypeId;
    }

    public void setInventoryTypeId(Long inventoryTypeId) {
        this.inventoryTypeId = inventoryTypeId;
    }

    public List<SoldEntry> getSoldEntryList() {
        return soldEntryList;
    }

    public void setSoldEntryList(List<SoldEntry> soldEntryList) {
        this.soldEntryList = soldEntryList;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
