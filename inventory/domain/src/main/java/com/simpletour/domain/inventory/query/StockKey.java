package com.simpletour.domain.inventory.query;

import com.simpletour.domain.inventory.InventoryType;

/**
 * 文件描述：库存所依赖字段的封装类
 * 创建人员：石广路
 * 创建日期：2015/11/30 15:27
 * 备注说明：null
 */
public class StockKey {
    private InventoryType inventoryType;

    private Long inventoryId;

    private int offset;

    public StockKey(InventoryType inventoryType, Long inventoryId) {
        this.inventoryType = inventoryType;
        this.inventoryId = inventoryId;
    }

    public StockKey(InventoryType inventoryType, Long inventoryId, int offset) {
        this.inventoryType = inventoryType;
        this.inventoryId = inventoryId;
        this.offset = offset;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockKey)) return false;

        StockKey stockKey = (StockKey) o;
        if (inventoryType != stockKey.inventoryType) return false;
        if (offset != stockKey.offset) return false;
        return !(inventoryId != null ? !inventoryId.equals(stockKey.inventoryId) : stockKey.inventoryId != null);
    }

    @Override
    public int hashCode() {
        int result = inventoryType != null ? inventoryType.hashCode() : 0;
        result = 31 * result + (inventoryId != null ? inventoryId.hashCode() : 0);
        result = 31 * result + 0 < offset ? Integer.hashCode(offset) : 0;
        return result;
    }

    @Override
    public String toString() {
        return "StockKey{" +
                "inventoryType=" + inventoryType +
                ", inventoryId=" + inventoryId +
                ", offset=" + offset +
                '}';
    }
}
