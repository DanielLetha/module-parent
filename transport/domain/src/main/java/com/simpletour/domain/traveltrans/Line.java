package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 线路
 * Created by Mario on 2015/11/21.
 */
@Entity
@Table(name = "TRANS_LINE")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class Line extends BaseDomain {

    public Line() {
    }

    public Line(List<BusNoSerial> busNoSeries, String name) {
        this.busNoSeries = busNoSeries;
        this.name = name;
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 供应商编号
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 线路名称
     */
    @Column
    private String name;

    @OrderBy(" day ASC ,sort ASC ")
    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<BusNoSerial> busNoSeries;

    @Version
    private Integer version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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

    public List<BusNoSerial> getBusNoSeries() {
        if (busNoSeries == null) {
            busNoSeries = new ArrayList<>();
        }
        return busNoSeries;
    }

    public void setBusNoSeries(List<BusNoSerial> busNoSeries) {
        this.busNoSeries = busNoSeries;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Line)) {
            return false;
        }
        Line other = (Line) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
