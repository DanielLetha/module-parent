package com.simpletour.domain.inventory.query;

import com.simpletour.domain.inventory.InventoryType;

/**
 * 文件描述：销售库存所依赖字段的封装类
 * 创建人员：石广路
 * 创建日期：2015/12/8 10:28
 * 备注说明：null
 */
public class SoldEntryKey extends StockKey {
    private Long orderId;

    private Long itemId;

    private Long parentId;

    public SoldEntryKey(InventoryType inventoryType, Long inventoryTypeId) {
        super(inventoryType, inventoryTypeId);
    }

    public SoldEntryKey(Long orderId, Long itemId, Long parentId) {
        super(null, null);
        this.orderId = orderId;
        this.itemId = itemId;
        this.parentId = parentId;
    }

    public SoldEntryKey(InventoryType inventoryType, Long inventoryTypeId, Long orderId, Long itemId, Long parentId) {
        super(inventoryType, inventoryTypeId);
        this.orderId = orderId;
        this.itemId = itemId;
        this.parentId = parentId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getParentId() {
        return parentId;
    }
}
