package com.simpletour.domain.order;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.domain.sale.SaleApp;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mario on 2016/4/20.
 */
@Entity
@Table(name = "ORD_ORDER")
public class Order extends BaseDomain {

    public enum Status {
        PENDING("等待确认", "pending", new String[]{"finished", "canceled", "closed"}),
        CANCELED("已取消", "canceled", true),
        CLOSED("已关闭", "closed", true),
        FINISHED("已完成", "finished", new String[]{"canceled"});

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

    @JoinColumn(name = "app_id")
    @ManyToOne
    private SaleApp saleApp;

    @Column
    private Integer amount;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "reserve_time")
    private Long reserveTime;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Item> items;

    @Version
    private Integer version;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    public SaleApp getSaleApp() {
        return saleApp;
    }

    public void setSaleApp(SaleApp saleApp) {
        this.saleApp = saleApp;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Long reserveTime) {
        this.reserveTime = reserveTime;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
