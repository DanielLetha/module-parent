package com.simpletour.domain.sale;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.domain.product.Product;

import javax.persistence.*;
import java.util.List;


/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/19.
 * Remark:  产品退改规则的实体类
 */
@Entity
@Table(name = "SALE_AGREEMENT_PRODUCT")
public class AgreementProduct extends BaseDomain{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 业务系统产品
     */
    @OneToOne
    @Column(name = "product_id")
    private Product product;

    /**
     * 销售协议
     */
    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    /**
     * 产品退款细则
     */
    @OneToMany(mappedBy = "agreementProduct",cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OrderBy("timing asc")
    private List<AgreementProductRefundRule> productRefundRules;

    /**
     * 退款规则文字说明
     */
    @Column(length=1024)
    private String refund;

    /**
     * 截止下单时间，相对于出行那天的 0:00点，正数表示0点以后，负数表示0点之前
     */
    @Column
    private Integer deadline;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public List<AgreementProductRefundRule> getProductRefundRules() {
        return productRefundRules;
    }

    public void setProductRefundRules(List<AgreementProductRefundRule> productRefundRules) {
        this.productRefundRules = productRefundRules;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
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
