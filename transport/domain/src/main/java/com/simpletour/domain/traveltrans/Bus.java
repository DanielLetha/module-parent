package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.DependEntity;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 客运汽车信息
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_BUS")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class Bus extends BaseDomain implements IDependTracable {


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
     * 车辆座位布局
     * 外键id:layout_id
     */
    @ManyToOne
    @JoinColumn(name = "layout_id")
    private SeatLayout layout;

    /**
     * 车辆型号
     */
    @Column
    private String model;

    /**
     * 车牌号
     */
    @Column
    private String license;

    /**
     * 车辆是否可用
     */
    @Column
    private boolean online = false;

    @Version
    private Integer version;

    /**
     * constructor
     */
    public Bus() {

    }


    /**
     * constructor
     *
     * @param layout
     * @param model
     * @param license
     * @param online
     */
    public Bus(SeatLayout layout, String model, String license, boolean online) {
        this.layout = layout;
        this.model = model;
        this.license = license;
        this.online = online;
    }

    public Bus(Bus bus) {
        this.id = bus.id;
        this.tenantId = bus.tenantId;
        this.layout = bus.layout;
        this.model = bus.model;
        this.license = bus.license;
        this.online = bus.online;
        this.version = bus.version;
    }

    /**
     * setter & getter
     */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public SeatLayout getLayout() {
        return layout;
    }

    public void setLayout(SeatLayout layout) {
        this.layout = layout;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

//    @Override
//    public List<DependEntity> getDependEntities() {
//        List<DependEntity> dependEntities = new ArrayList<>();
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(Bus.class), this.id, QueryUtil.getTableName(SeatLayout.class), this.layout.getId()));
//        return dependEntities;
//    }

    @Override
    @JSONField(serialize = false)
    public List<Dependency> getDependencies() {
        return Arrays.asList(new Dependency(layout.getEntityKey()));
    }
}
