package com.simpletour.domain.order;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.domain.inventory.query.IStockAvailable;
import com.simpletour.domain.inventory.query.StockAvailableKey;
import com.simpletour.domain.sale.SaleApp;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Mario on 2016/4/20.
 */
@Entity
@Table(name = "ORD_ITEM")
public class Item extends BaseDomain implements IStockAvailable {

    @Override
    public List<StockAvailableKey> getStockAvailableEntries() {
        return null;
    }

    public enum Status {
        PENDING("等待确认", "pending", new String[]{"finished", "canceled", "closed"}),
        CANCELED("已取消", "canceled", true),
        CLOSED("已关闭", "closed", true),
        REFUND("已退款", "refunded", true),
        FINISHED("已完成", "finished", new String[]{"canceled", "refunding"}),
        REFUNDING("申请退款", "refunding", new String[]{"refunded"});

        private String remark, value;

        /**
         * 是否更新库存
         */
        private boolean flushStock = false;
        /**
         * 可以转变的状态
         */
        private List<String> transitions;

        Status(String remark, String value, boolean flushStock) {
            this.remark = remark;
            this.value = value;
            this.flushStock = flushStock;
        }

        Status(String remark, String value, String[] transitions) {
            this.remark = remark;
            this.value = value;
            this.transitions = Arrays.asList(transitions);
        }

        public String getRemark() {
            return remark;
        }

        public String getValue() {
            return value;
        }

        public boolean isFlushStock() {
            return flushStock;
        }

        public List<String> getTransitions() {
            return transitions;
        }
    }

    @Id()
    @Column(name = "ID")
    private Long id;

    /**
     * 对应销售端
     */
    @ManyToOne
    @JoinColumn(name = "app_id")
    private SaleApp saleApp;

    /**
     * 对应订单
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 联系人姓名
     */
    @Column
    private String name;

    /**
     * 联系人电话
     */
    @Column
    private String mobile;

    /**
     * 对应产品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 对应产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 对应产品类型:bus("车位"), hotel("住宿"), scenic("景点"), catering("餐饮"), entertainment("娱乐"), other("其他");
     */
    @Column(name = "product_type")
    private String productType;

    /**
     * 订单项使用日期
     */
    @Column
    @Temporal(value = TemporalType.DATE)
    private Date day;

    /**
     * 订购产品数量
     */
    @Column
    private Integer quantity;

    /**
     * 成人成本单价,销售协议中规定的成人类型产品的成本价格
     */
    @Column(name = "adult_cost")
    private Integer adultCost;

    /**
     * 儿童成本单价,销售协议中规定的儿童类型产品的成本价格
     */
    @Column(name = "child_cost")
    private Integer childCost;

    /**
     * 成人结算单价,该产品在该销售端售卖的成人单价价格
     */
    @Column(name = "adult_settlement")
    private Integer adultSettlement;

    /**
     * 儿童结算单价,该产品在该销售端售卖的儿童单价价格
     */
    @Column(name = "child_settlement")
    private Integer childSettlement;

    /**
     * 成本总价:成本单价*订购产品数量 = (adultCost + childCost)*quantity
     */
    @Column(name = "total_cost")
    private Integer totalCost;

    /**
     * 结算总价:结算单价*订购产品数量 = (adultSettlement + childSettlement)*quantity
     */
    @Column(name = "total_settlement")
    private Integer totalSettlement;

    /**
     * 产品的快照信息:只包含车次和元素的一些基本信息
     */
    @Column(columnDefinition = "text")
    private String snapshot;

    /**
     * 退改规则的快照信息
     */
    @Column(columnDefinition = "text")
    private String refund;

    /**
     * 订单项的状态
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 订单项备注
     */
    @Column
    private String remark;

    @OneToMany(mappedBy = "item", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Customer> customers;

    @Version
    private Integer version;

    @Override
    public Long getId() {
        return this.id;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAdultCost() {
        return adultCost;
    }

    public void setAdultCost(Integer adultCost) {
        this.adultCost = adultCost;
    }

    public Integer getChildCost() {
        return childCost;
    }

    public void setChildCost(Integer childCost) {
        this.childCost = childCost;
    }

    public Integer getAdultSettlement() {
        return adultSettlement;
    }

    public void setAdultSettlement(Integer adultSettlement) {
        this.adultSettlement = adultSettlement;
    }

    public Integer getChildSettlement() {
        return childSettlement;
    }

    public void setChildSettlement(Integer childSettlement) {
        this.childSettlement = childSettlement;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getTotalSettlement() {
        return totalSettlement;
    }

    public void setTotalSettlement(Integer totalSettlement) {
        this.totalSettlement = totalSettlement;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
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

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
