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
 * 目的地
 * <p>
 * Created by Jeff.Song on 2015/11/18.
 */
@Entity
@Table(name = "TR_DESTINATION")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Destination extends LogicalDeletableDomain implements IUnionEntityKey, IDependTracable {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 租户信息
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 目的地所在地编码
     */
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    /**
     * 目的地名称
     */
    @Column
    private String name;

    /**
     * 目的地详细地址
     */
    @Column
    private String address;

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
    @JSONField(serialize = false)
    private static UnionEntityKey unionEntityKey;

    public Destination(){}

    public Destination(String name, String address, String remark, Area area, BigDecimal lon, BigDecimal lat) {
        this.area = area;
        this.name = name;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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
        if (null == name || null == id) {
            return null;
        }

        if (null == unionEntityKey) {
            unionEntityKey = new UnionEntityKey(Destination.class, name, this);
        } else {
            unionEntityKey.setName(name);
            unionEntityKey.setDestination(this);
        }

        return unionEntityKey;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencyList = new ArrayList<>();
        dependencyList.add(new Dependency(area.getEntityKey()));
        return dependencyList;
    }
}
