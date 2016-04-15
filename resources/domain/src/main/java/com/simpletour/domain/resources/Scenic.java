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
 * 景区
 * <p>
 * Created by Jeff.Song on 2015/11/19.
 */
@Entity
@Table(name = "TR_SCENIC")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Scenic extends LogicalDeletableDomain implements IUnionEntityKey, IDependTracable {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     *
     */
    @Column
    private String name;

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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JSONField(serialize = false)
    private Scenic parent;

    @OneToMany(mappedBy = "parent")
    @JSONField(serialize = false)
    private List<Scenic> scenics;

    @Transient
    private static UnionEntityKey unionEntityKey;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
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

    public Scenic getParent() {
        return parent;
    }

    public void setParent(Scenic parent) {
        this.parent = parent;
    }

    public List<Scenic> getScenics() {
        return scenics;
    }

    public void setScenics(List<Scenic> scenics) {
        this.scenics = scenics;
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
            unionEntityKey = new UnionEntityKey(Scenic.class, name, destination);
        } else {
            unionEntityKey.setName(name);
            unionEntityKey.setDestination(destination);
        }

        return unionEntityKey;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencyList = new ArrayList<>();
        dependencyList.add(new Dependency(destination.getEntityKey()));
        if (this.parent != null) {
            dependencyList.add(new Dependency(parent.getEntityKey()));
        }
        return dependencyList;
    }
}
