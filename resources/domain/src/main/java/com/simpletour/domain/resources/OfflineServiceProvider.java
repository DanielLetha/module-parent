package com.simpletour.domain.resources;

import com.simpletour.commons.data.domain.LogicalDeletableDomain;

import javax.persistence.*;

/**
 * 线下供应商实体
 * <p>
 * Created by Jeff.Song on 2015/11/18.
 */
@Entity
@Table(name = "TR_OSP")
public class OfflineServiceProvider extends LogicalDeletableDomain {

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
     * 供应商名称
     */
    @Column
    private String name;

    /**
     * 备注信息
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 版本信息，用于可能并发请求时的乐观锁处理
     */
    @Version
    private Integer version;

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
}
