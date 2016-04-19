package com.simpletour.domain.sale;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * 销售协议实体类
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
@Entity
@Table(name = "SALE_PROTOCOL")
public class Agreement extends BaseDomain {
    /**
     * 销售协议状态枚举
     */
    public enum Status {
        enable("启用"), disable("禁用");

        private String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    /**
     * 销售协议主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    /**
     * 销售端
     */
    @OneToOne
    @JoinColumn(name = "app_id")
    private SaleApp saleApp;
    /**
     * 公司ID
     */
    @Column(name = "tenant_id")
    private Long tenantId;
    /**
     * 状态
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.enable;
    /**
     * 备注
     */
    @Column
    private String remark;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public Agreement() {
    }

    public Agreement(Long tenantId, SaleApp saleApp, Status status, String remark) {
        this.tenantId = tenantId;
        this.saleApp = saleApp;
        this.status = status;
        this.remark = remark;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public SaleApp getSaleApp() {
        return saleApp;
    }

    public void setSaleApp(SaleApp saleApp) {
        this.saleApp = saleApp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
