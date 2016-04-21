package com.simpletour.domain.inventory;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件描述：库存关联实体基类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/20 10:16
 * 备注说明：null
 * @since 2.0-SNAPSHOT
 */
@MappedSuperclass
public abstract class StockBound extends BaseDomain implements IDependTracable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 库存对象类型，只包含元素和车次
     */
    @Column(name = "row_table", nullable = false)
    @Enumerated(EnumType.STRING)
    protected InventoryType inventoryType;

    /**
     * 库存对象Id，对应元素和车次数据表的主键（id）
     */
    @Column(name = "row_id", nullable = false)
    protected Long inventoryId;

    /**
     * 库存日期
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    protected Date day;

    public StockBound() {

    }

    public StockBound(InventoryType inventoryType, Long inventoryId, Date day) {
        this.inventoryType = inventoryType;
        this.inventoryId = inventoryId;
        this.day = day;
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencyList = new ArrayList<>(1);
        dependencyList.add(new Dependency(inventoryType.getTabelName(), inventoryId));
        return dependencyList;
    }
}
