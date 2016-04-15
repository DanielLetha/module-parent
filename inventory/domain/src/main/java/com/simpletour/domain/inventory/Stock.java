package com.simpletour.domain.inventory;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by igotti on 15-11-19.
 * 库存实体，表示特定某一天，库存主体可售数量，售价等。
 * 需要提供操作日志的支持
 */
@Entity
@Table(name = "INV_STOCK")
public class Stock extends BaseDomain {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 库存类型，包括行程、产品、元素、车次
     */
    @Column
    @Enumerated(EnumType.STRING)
    private InventoryType inventoryType;

    /**
     * 库存类型Id，对应行程、产品、元素、车次表（对象）的主键（id）
     */
    @Column
    private Long inventoryTypeId;

    /**
     * 缓存字段：库存主体名称
     * added by shiguanglu at 2015-12-15 18:12
     */
    @Transient
    private String inventoryName;

    /**
     * 缓存字段：库存备注信息
     * added by shiguanglu at 2015-12-22 12:04
     */
    @Transient
    private String inventoryRemark;

    /**
     * 库存日期，只表示day指示的那天的库存
     */
    @Column
    @Temporal(value = TemporalType.DATE)
    private Date day;

    /**
     * 库存数量，只表示day指示的那天的库存数量
     */
    @Column
    private Integer quantity;

    /**
     * 缓存字段：添加已售库存量
     * added by shiguanglu at 2015-11-27 15:42
     */
    @Transient
    private Integer soldQuantity;

    /**
     * 缓存字段：添加可售库存量
     * added by shiguanglu at 2015-11-27 15:45
     */
    @Transient
    private Integer availableQuantity;

    /**
     * 库存价格，只表示day指示的那天的库存价格
     */
    @Column
    private BigDecimal price;

    /**
     * 库存是否下线，默认为上线
     */
    @Column
    private Boolean online = true;

    /**
     * version
     */
    @Version
    @Column
    private Long version;

    public Stock(){
    }

    /**
     * constructor
     * @param inventoryType
     * @param inventoryTypeId
     * @param day
     * @param quantity
     * @param price
     * @param online
     * @param version
     */
    public Stock(InventoryType inventoryType, Long inventoryTypeId, Date day, Integer quantity, BigDecimal price, Boolean online, Long version) {
        this.inventoryType = inventoryType;
        this.inventoryTypeId = inventoryTypeId;
        this.day = day;
        this.quantity = quantity;
        this.price = price;
        this.online = online;
        this.version = version;
    }

    /**
     * 对应前端添加一个库存时候的表单数据
     * @param inventoryType
     * @param inventoryTypeId
     * @param day
     * @param quantity
     * @param price
     * @param online
     */
    public Stock(InventoryType inventoryType, Long inventoryTypeId, Date day, Integer quantity, BigDecimal price, Boolean online) {
        this.inventoryType = inventoryType;
        this.inventoryTypeId = inventoryTypeId;
        this.day = day;
        this.quantity = quantity;
        this.price = price;
        this.online = online;
    }

    /**
     * 提供一个全属性带参构造方法
     * @author: 石广路
     * @param inventoryType
     * @param inventoryTypeId
     * @param day
     * @param quantity
     * @param soldQuantity
     * @param availableQuantity
     * @param price
     * @param online
     * @param version
     */
    public Stock(InventoryType inventoryType, Long inventoryTypeId, Date day, Integer quantity, Integer soldQuantity, Integer availableQuantity, BigDecimal price, Boolean online, Long version) {
        this.inventoryType = inventoryType;
        this.inventoryTypeId = inventoryTypeId;
        this.day = day;
        this.quantity = quantity;
        this.soldQuantity = soldQuantity;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.online = online;
        this.version = version;
    }

    /**
     * 获取经过日期类型转换的库存日期
     * @author 石广路
     * @return  java.util.Date
     */
    public Date getDay() {
        //return DateTimeTools.convertSqlDate2UtilDate(day);
        return day;
    }

    /**
     * 设置经过日期类型转换的库存日期
     * @author 石广路
     * @return  java.util.Date
     */
    public void setDay(Date day) {
        //this.day = DateTimeTools.convertSqlDate2UtilDate(day);
        this.day = day;
    }

    public Integer getQuantity() {
        return null == quantity ? 0 : quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return null == soldQuantity ? 0 : soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Long getInventoryTypeId() {
        return inventoryTypeId;
    }

    public void setInventoryTypeId(Long inventoryTypeId) {
        this.inventoryTypeId = inventoryTypeId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryRemark() {
        return inventoryRemark;
    }

    public void setInventoryRemark(String inventoryRemark) {
        this.inventoryRemark = inventoryRemark;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;

        Stock stock = (Stock) o;

        if (getInventoryType() != stock.getInventoryType()) return false;
        if (getInventoryTypeId() != null ? !getInventoryTypeId().equals(stock.getInventoryTypeId()) : stock.getInventoryTypeId() != null)
            return false;
        if (inventoryName != null ? !inventoryName.equals(stock.getInventoryName()) : stock.getInventoryName() != null)
            return false;
        if (inventoryRemark != null ? !inventoryRemark.equals(stock.getInventoryRemark()) : stock.getInventoryRemark() != null)
            return false;
        if (getDay() != null ? !getDay().equals(stock.getDay()) : stock.getDay() != null) return false;
        if (getQuantity() != null ? !getQuantity().equals(stock.getQuantity()) : stock.getQuantity() != null)
            return false;
        if (getSoldQuantity() != null ? !getSoldQuantity().equals(stock.getSoldQuantity()) : stock.getSoldQuantity() != null)
            return false;
        if (getAvailableQuantity() != null ? !getAvailableQuantity().equals(stock.getAvailableQuantity()) : stock.getAvailableQuantity() != null)
            return false;
        if (getPrice() != null ? !getPrice().equals(stock.getPrice()) : stock.getPrice() != null) return false;
        if (getOnline() != null ? !getOnline().equals(stock.getOnline()) : stock.getOnline() != null) return false;
        return !(getVersion() != null ? !getVersion().equals(stock.getVersion()) : stock.getVersion() != null);
    }

    @Override
    public int hashCode() {
        int result = getInventoryType() != null ? getInventoryType().hashCode() : 0;
        result = 31 * result + (getInventoryTypeId() != null ? getInventoryTypeId().hashCode() : 0);
        result = 31 * result + (inventoryName != null ? inventoryName.hashCode() : 0);
        result = 31 * result + (inventoryRemark != null ? inventoryRemark.hashCode() : 0);
        result = 31 * result + (getDay() != null ? getDay().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getSoldQuantity() != null ? getSoldQuantity().hashCode() : 0);
        result = 31 * result + (getAvailableQuantity() != null ? getAvailableQuantity().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getOnline() != null ? getOnline().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", inventoryType=" + inventoryType +
                ", inventoryTypeId=" + inventoryTypeId +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventoryRemark='" + inventoryRemark + '\'' +
                ", day=" + day +
                ", quantity=" + quantity +
                ", soldQuantity=" + soldQuantity +
                ", availableQuantity=" + availableQuantity +
                ", price=" + price +
                ", online=" + online +
                ", version=" + version +
                '}';
    }
}
