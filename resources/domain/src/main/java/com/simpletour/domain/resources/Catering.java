package com.simpletour.domain.resources;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 餐饮点
 * <p>
 * Created by Jeff.Song on 2015/11/19.
 */
@Entity
@Table(name = "TR_CATERING")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Catering extends LogicalDeletableDomain implements IUnionEntityKey, IDependTracable {

    public enum Type {
        hotel("酒店"), other("其他");

        private String remark;

        Type(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return this.remark;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 餐饮项目名称
     */
    @Column
    private String name;

    /**
     * 餐饮类型
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;
    /**
     * 餐饮地点
     */
    @Column
    private String address;

    /**
     * 目的地
     */
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     * 位置信息(经度)
     */
    @Column
    private BigDecimal lon;

    /**
     * 位置信息(纬度)
     */
    @Column
    private BigDecimal lat;

    /**
     * 备注信息
     * 由石广路添加于2015年12月24日，用以解决前端带格式的备注信息过长的问题
     */
    @Column(columnDefinition = "text")
    private String remark;

    @Version
    private Integer version;

    @Transient
    private static UnionEntityKey unionEntityKey;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @JSONField(serialize = false)
    public UnionEntityKey getUnionEntityKey() {
        if (null == name || null == destination) {
            return null;
        }

        if (null == unionEntityKey) {
            unionEntityKey = new UnionEntityKey(Catering.class, name, destination);
        } else {
            unionEntityKey.setName(name);
            unionEntityKey.setDestination(destination);
        }

        return unionEntityKey;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependEntities = new ArrayList<>(1);
        dependEntities.add(new Dependency(destination.getEntityKey()));
        return dependEntities;
    }

}
