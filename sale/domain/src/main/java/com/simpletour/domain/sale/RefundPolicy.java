package com.simpletour.domain.sale;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 14:45
 */
@Entity
@Table(name = "SALE_REFUND_POLICY")
public class RefundPolicy extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 退款规则
     */
    @OneToMany(mappedBy = "refundPolicy", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("timing asc")
    private List<RefundRule> refundRules;

    /**
     * 退款模板名称
     */
    @Column
    private String name;

    /**
     * 公司ID
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 版本号（乐观锁）
     */
    @Version
    private Integer version;

    public RefundPolicy() {
    }

    public RefundPolicy(String name, List<RefundRule> refundRules, String remark) {
        this.name = name;
        this.refundRules = refundRules;
        this.remark = remark;
    }

    public RefundPolicy(Long id, Integer version, String name, List<RefundRule> refundRules, String remark) {
        this(name, refundRules, remark);
        this.id = id;
        this.version = version;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
