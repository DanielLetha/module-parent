package com.simpletour.domain.sale;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 14:49
 */
@Entity
@Table(name = "SALE_REFUND_RULE")
public class RefundRule extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 关联的模块
     */
    @ManyToOne
    @JoinColumn(name = "template_id")
    private RefundPolicy refundPolicy;

    /**
     * 时间范围（距出现当天0点，单位天）
     */
    @Column
    private Integer timing;

    /**
     * 退款百分比（0到100之间）
     */
    @Column
    private Integer ration;

//    /**
//     * 公司ID
//     */
//    @Column(name = "tenant_id")
//    private Long tenantId;


    /**
     * 版本号（乐观锁）
     */
    @Version
    private Integer version;

    public RefundRule() {
    }

    public RefundRule(Integer timing,Integer ration){
        this.timing=timing;
        this.ration=ration;
    }

    public RefundRule(Long id,Integer version,Integer timing,Integer ration){
        this(timing, ration);
        this.id=id;
        this.version=version;
    }

    public RefundRule(RefundPolicy refundPolicy, Integer timing, Integer ration) {
        this.refundPolicy = refundPolicy;
        this.timing = timing;
        this.ration = ration;
    }

    public RefundRule(Long id, Integer version, RefundPolicy refundPolicy, Integer timing, Integer ration) {
        this(refundPolicy, timing, ration);
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

    public RefundPolicy getRefundPolicy() {
        return refundPolicy;
    }

    public void setRefundPolicy(RefundPolicy refundPolicy) {
        this.refundPolicy = refundPolicy;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public Integer getRation() {
        return ration;
    }

    public void setRation(Integer ration) {
        this.ration = ration;
    }

//    public Long getTenantId() {
//        return tenantId;
//    }
//
//    public void setTenantId(Long tenantId) {
//        this.tenantId = tenantId;
//    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
