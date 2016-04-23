package com.simpletour.domain.inventory.query;

import com.simpletour.domain.inventory.InventoryType;

/**
 * 文件描述：库存销售记录查询参数封装类
 * 创建人员：石广路
 * 创建日期：2015/12/8 10:28
 * 备注说明：在2.0-SNAPSHOT版本中去掉了之前版本中的orderId查询参数，增加了valid查询参数
 * @since 2.0-SNAPSHOT
 */
public class SoldEntryKey extends StockKey {
//    private Long orderId;

    private Long itemId;

    private Long parentId;

    private Boolean valid = true;

    public SoldEntryKey(InventoryType inventoryType, Long inventoryId) {
        super(inventoryType, inventoryId);
    }

    public SoldEntryKey(Long itemId, Long parentId, Boolean valid) {
        super(null, null);
//        this.orderId = orderId;
        this.itemId = itemId;
        this.parentId = parentId;
        this.valid = null == valid ? true : valid;
    }

    public SoldEntryKey(InventoryType inventoryType, Long inventoryId, Long itemId, Long parentId, Boolean valid) {
        super(inventoryType, inventoryId);
//        this.orderId = orderId;
        this.itemId = itemId;
        this.parentId = parentId;
        this.valid = null == valid ? true : valid;
    }

    public Long getItemId() {
        return itemId;
    }

//    public Long getOrderId() {
//        return orderId;
//    }

    public Long getParentId() {
        return parentId;
    }

    public Boolean isValid() {
        return valid;
    }
}
