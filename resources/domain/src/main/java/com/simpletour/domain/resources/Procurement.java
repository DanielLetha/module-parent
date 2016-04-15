package com.simpletour.domain.resources;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 产品元素
 * <p>
 * Created by Jeff.Song on 2015/11/18.
 */
@Entity
@Table(name = "TR_PROCUREMENT")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Procurement extends LogicalDeletableDomain implements IDependTracable, IStockTraceable {

    public enum ResourceType {
        hotel("住宿"),
        scenic("景点"),
        catering("餐饮"),
        entertainment("娱乐");

        String remark;

        ResourceType(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 租户id
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 元素提供的资源类型，对应着row_table
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    /**
     * 资源ID，对应着row_id
     */
    @Column
    private Long resourceId;

    /**
     * 备注信息
     * 由石广路添加于2015年12月24日，用以解决前端带格式的备注信息过长的问题
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 元素名称
     */
    @Column
    private String name;

    /**
     * 住宿点所在区域编号
     */
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     * 状态：true：上线，false：下线
     */
    @Column
    private Boolean online = true;

    @Version
    private Integer version;

    /**
     * 产品提供商
     */
    @ManyToOne
    @JoinColumn(name = "osp_id")
    private OfflineServiceProvider osp;

    public Procurement() {
    }

    public Procurement(Long tenantId, ResourceType resourceType, Long resourceId, String remark, String name, Destination destination, Boolean online, Integer version, OfflineServiceProvider osp) {
        this.tenantId = tenantId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.remark = remark;
        this.name = name;
        this.destination = destination;
        this.online = online;
        this.version = version;
        this.osp = osp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public OfflineServiceProvider getOsp() {
        return osp;
    }

    public void setOsp(OfflineServiceProvider osp) {
        this.osp = osp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
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
    public StockKey getStockKey() {
        return new StockKey(InventoryType.procurement, id);
    }

    @Override
    @JSONField(serialize = false)
    public List<StockKey> getDependentStockKeys() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Procurement)) return false;
        Procurement that = (Procurement) o;
        return that.getStockKey().equals(getStockKey());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result += InventoryType.valueOf("procurement").hashCode();
        return result;
    }

    @Override
    public List<Dependency> getDependencies() {
        Class clazz;
        if (ResourceType.hotel == resourceType) {
            clazz = Hotel.class;
        } else if (ResourceType.scenic == resourceType) {
            clazz = Scenic.class;
        } else if (ResourceType.catering == resourceType) {
            clazz = Catering.class;
        } else if (ResourceType.entertainment == resourceType) {
            clazz = Entertainment.class;
        } else {
            return Collections.emptyList();
        }
        List<Dependency> dependencyList = new ArrayList<>(2);
        dependencyList.add(new Dependency(QueryUtil.getTableName(clazz), resourceId));
        dependencyList.add(new Dependency(osp.getEntityKey()));
        return dependencyList;
    }
}
