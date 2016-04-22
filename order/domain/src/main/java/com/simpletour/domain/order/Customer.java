package com.simpletour.domain.order;

import com.simpletour.commons.data.domain.BaseDomain;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;

/**
 * Created by Mario on 2016/4/20.
 */
public class Customer extends BaseDomain {

    public enum Type {
        CHILD("child", "儿童"),
        ADULT("adult", "成人");
        private String remark, value;

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

    /**
     * 主键id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    /**
     * 关联的订单:财务订单
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    /**
     * 关联的订单项:业务订单
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type = Type.ADULT;

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
     * 身份证类型:1.身份证2.台胞证3.回乡证4.护照
     */
    @Column(name = "id_type")
    @Enumerated(EnumType.STRING)
    private IdType idType = IdType.ID;
    /**
     * 身份证号码
     */
    @Column(name = "id_no")
    private String idNo;
    /**
     * 联系邮箱
     */
    @Column
    private String email;

    /**
     * 凭证代码
     */
    @Column
    private String code;

    /**
     * 凭证的状态,标识出行人是否是可用的,默认为true.
     *
     * @remark 这个字段是一个冗余字段, 如果不加这个字段, 相关进行customer查询的时候每次都会去反查一次该customer关联的订单，
     * 并判断订单状态是否满足相关需求,进而对查询出来的customer列表进行部分屏蔽
     */
    @Column
    private Boolean valid = true;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    @Version
    private Integer version;

    public Customer() {
    }

    public Customer(Order order, Item item, Type type, String name, String mobile, IdType idType, String idNo, String email) {
        this.order = order;
        this.item = item;
        this.type = type;
        this.name = name;
        this.mobile = mobile;
        this.idType = idType;
        this.idNo = idNo;
        this.email = email;
    }

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
