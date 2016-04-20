package com.simpletour.domain.sale;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/19.
 * Remark: 产品退款细则
 */
@Entity
@Table(name = "SALE_AGREEMENT_PRODUCT_REFUND_RULE")
public class AgreementProductRefundRule extends BaseDomain{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 产品退改规则的实体类
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private AgreementProduct agreementProduct;

    /**
     * 时间范围（距出现当天0点，单位小时）
     */
    @Column
    private Integer timing;

    /**
     * 退款百分比（0到100之间）
     */
    @Column
    private Integer ratio;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AgreementProduct getAgreementProduct() {
        return agreementProduct;
    }

    public void setAgreementProduct(AgreementProduct agreementProduct) {
        this.agreementProduct = agreementProduct;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
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
