package com.simpletour.domain.inventory;

import javax.persistence.*;
import java.util.Date;

/**
 * 文件描述：库存价格实体类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/19 16:05
 * 备注说明：从库存表中新拆分出来的数据表
 * @since 2.0-SNAPSHOT
 */
@Entity
@Table(name = "INV_PRICE")
public class Price extends StockBound {
    public enum Type {
        adult("成人"), child("儿童");

        private String remark;

        Type(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    /**
     * 库存价格类型，只表示day指示的那天的库存价格
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type = Type.adult;

    /**
     * 成本价
     */
    @Column
    private Integer cost;

    /**
     * 结算价
     */
    @Column
    private Integer settlement;

    /**
     * 建议售价
     */
    @Column
    private Integer retail;

    public Price() {
    }

    public Price(InventoryType inventoryType, Long inventoryId, Date day, Type type, Integer cost, Integer settlement, Integer retail) {
        super(inventoryType, inventoryId, day);
        this.type = type;
        this.cost = cost;
        this.settlement = settlement;
        this.retail = retail;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getSettlement() {
        return settlement;
    }

    public void setSettlement(Integer settlement) {
        this.settlement = settlement;
    }

    public Integer getRetail() {
        return retail;
    }

    public void setRetail(Integer retail) {
        this.retail = retail;
    }
}
