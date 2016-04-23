package com.simpletour.biz.sale.bo;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/22 18:15
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public class Price {

    private Long id;

    private Integer cost;

    private Integer settlement;

    private Integer retail;

    private Integer version;

    public Price(Integer cost, Integer settlement, Integer retail,Integer version) {
        this.cost = cost;
        this.settlement = settlement;
        this.retail = retail;
        this.version = version;
    }

    public Price(Long id, Integer cost, Integer settlement, Integer retail, Integer version) {
        this.id = id;
        this.cost = cost;
        this.settlement = settlement;
        this.retail = retail;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
