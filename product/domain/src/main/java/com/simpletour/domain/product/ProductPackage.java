package com.simpletour.domain.product;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.DependEntity;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Procurement;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品（行程）打包
 * <p>
 * 实际就是产品（行程）与元素的多对多中间关系对象，带偏移状态
 * <p>
 * Created by Jeff.Song on 2015/11/20.
 */
@Entity
@Table(name = "PROD_PACKAGE")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
//@DataChangeTraceable
public class ProductPackage extends BaseDomain {

    public ProductPackage() {

    }

    public ProductPackage(Procurement procurement, Integer offset) {
        this.procurement = procurement;
        this.offset = offset;
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 产品
     */
    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JSONField(serialize = false)
    private Product product;

    /**
     * 目的地
     */
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     * 元素对象
     */
    @ManyToOne
    @JoinColumn(name = "procurement_id")
    private Procurement procurement;

    /**
     * 元素在产品包中的偏移
     */
    @Column(name = "\"offset\"")
    private Integer offset;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Procurement getProcurement() {
        return procurement;
    }

    public void setProcurement(Procurement procurement) {
        this.procurement = procurement;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @JSONField(serialize = false)
    List<DependEntity> getDependEntities(String rowTable, Long rowId) {
        List<DependEntity> dependEntities = new ArrayList<>();
        dependEntities.add(new DependEntity(rowTable, rowId, QueryUtil.getTableName(Procurement.class), procurement.getId()));
        dependEntities.add(new DependEntity(rowTable, rowId, QueryUtil.getTableName(Destination.class), procurement.getId()));
        return dependEntities;
    }
}
