package com.simpletour.domain.order;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * Created by Mario on 2016/4/20.
 */
public class Customer extends BaseDomain {

    public enum IdType {
        ID("身份证", "id"), TW_TRAVEL_PERMIT("台胞证", "tw_travel_permit"), HKM_TRAVEL_PERMIT("回乡证", "hkm_travel_permit"), PASSPORT("护照", "passport");
        private String remark, value;

        IdType(String remark, String value) {
            this.remark = remark;
            this.value = value;
        }

        public String getRemark() {
            return this.remark;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private String name;

    @Column
    private String mobile;

    @Column(name = "id_type")
    @Enumerated(EnumType.STRING)
    private IdType idType = IdType.ID;

    @Column(name = "id_no")
    private String idNo;

    @Column
    private String email;

    @Column(columnDefinition = "text")
    private String remark;

    @Version
    private Integer version;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
