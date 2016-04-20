package com.simpletour.domain.sale;


import com.simpletour.domain.product.Product;

import javax.persistence.*;
import java.util.Date;

/**
 * @Brief :  协议产品价格
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 15:18
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Entity
@Table(name="sale_agree_product_price")
public class AgreementProductPrice  {


    public enum Type{
        CHILD("child","儿童"),
        ADULT("adult","成人")
        ;
        private String remark,value;

        Type(String remark, String value) {
            this.remark = remark;
            this.value = value;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 协议
     */
    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
    /**
     * 产品
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    /**
     *类型
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    /**
     * 日期
     */
    @Column(name = "date")
    private Date date;

    /**
     * 成本
     */
    @Column(name = "cost")
    private Integer cost;
    /**
     * 结算价
     */
    @Column(name = "settlement")
    private Integer settlement;

    /**
     * 建议价
     */
    @Column(name = "retail")
    private Integer retail;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 版本号
     */
    @Version
    private Integer version;
//    /**
//     * 创建时间
//     */
//    @Column
//    @Temporal(value = T)
//    private Integer createTime;


    public AgreementProductPrice() {
    }

    public AgreementProductPrice(Agreement agreement, Product product, Type type, Date date, Integer cost, Integer settlement, Integer retail, String remark, Integer version) {
        this.agreement = agreement;
        this.product = product;
        this.type = type;
        this.date = date;
        this.cost = cost;
        this.settlement = settlement;
        this.retail = retail;
        this.remark = remark;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getSettlement() {
        return settlement;
    }

    public void setSettlement(Integer settlement) {
        this.settlement = settlement;
    }

    public Integer getRetail() {
        return retail;
    }

    public void setRetail(Integer retail) {
        this.retail = retail;
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
