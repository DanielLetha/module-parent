package com.simpletour.domain.product;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.domain.dependency.DependEntity;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.resources.Procurement;
import com.simpletour.domain.traveltrans.BusNo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品实体类
 * <p>
 * Created by Jeff.Song on 2015/11/20.
 */
@Entity
@Table(name = "PROD_PRODUCT")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
//@DataChangeTraceable
public class Product extends LogicalDeletableDomain implements IStockTraceable, IDependTracable {
    public Product() {
    }

    /**
     * constructor for product only
     * @param name
     * @param online
     * @param remark
     */
    public Product(String name, Boolean online, String remark, List productPackages) {
        this.name = name;
        this.online = online;
        this.remark = remark;
        this.productPackages = productPackages;
    }

    /**
     * constructor for bus only
     * @param name
     * @param shuttle
     * @param depart
     * @param arrive
     * @param departTime
     * @param arriveTime
     * @param tourismRouteList
     * @param online
     * @param remark
     */
    public Product(String name, boolean shuttle, String depart, String arrive, String departTime, String arriveTime, List<TourismRoute> tourismRouteList, Boolean online, String remark) {
        this.name = name;
        this.shuttle = shuttle;
        this.depart = depart;
        this.arrive = arrive;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.tourismRouteList = tourismRouteList;
        this.online = online;
        this.remark = remark;
    }

    /**
     * constructor for pkg product
     * @param name
     * @param shuttle
     * @param depart
     * @param arrive
     * @param departTime
     * @param arriveTime
     * @param tourismRouteList
     * @param productPackages
     * @param remark
     * @param online
     */
    public Product(String name, boolean shuttle, String depart, String arrive, String departTime, String arriveTime, List<TourismRoute> tourismRouteList, List<ProductPackage> productPackages, String remark, Boolean online) {
        this.name = name;
        this.shuttle = shuttle;
        this.depart = depart;
        this.arrive = arrive;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.tourismRouteList = tourismRouteList;
        this.productPackages = productPackages;
        this.remark = remark;
        this.online = online;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependEntities = new ArrayList<>();
        if (productPackages != null) {
            for (ProductPackage productPackage : productPackages) {
                dependEntities.addAll(productPackage.getDependEntities(QueryUtil.getTableName(Product.class), id));
            }
        }
        if (tourismRouteList != null) {
            for (TourismRoute tourismRoute : tourismRouteList) {
                dependEntities.addAll(tourismRoute.getDependEntities());
            }
        }
        return dependEntities;
    }

    public enum Type {
        // other: 针对数据迁移x+y 统一转为其他类型
        bus("车位"), hotel("住宿"), scenic("景点"), catering("餐饮"), entertainment("娱乐"), other("其他");

        private String remark;

        Type(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 租户信息
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 产品名称
     */
    @Column
    private String name;

    /**
     * 产品类型,
     */
    @Column
    private String type;

    /**
     * 产品天数
     */
    @Column
    private Integer days;

    /**
     * 是否往返
     */
    @Column
    private Boolean shuttle;

    /**
     * 出发地
     */
    @Column
    private String depart;

    /**
     * 到达地
     */
    @Column
    private String arrive;

    /**
     * 行程出发时间
     */
    @Column(name = "depart_time", length = 10)
    private String departTime;

    /**
     * 行程到达时间
     */
    @Column(name = "arrive_time", length = 10)
    private String arriveTime;

    /**
     * 行程的路线
     */
    // 级联更新，必须要orphanRemoval = true, cascade= {CascadeType.PERSIST}
    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "product", fetch = FetchType.LAZY)
    private List<TourismRoute> tourismRouteList = new ArrayList<>();

    /**
     * 状态：上线，下线
     */
    @Column
    private Boolean online = true;

    @Column
    private String remark;

    @Version
    private Integer version;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductPackage> productPackages = new ArrayList<>();

    @Transient
    private boolean isTourismRouteListSorted = false;

    @Override
    @JSONField(serialize = false)
    public StockKey getStockKey() {
        return new StockKey(InventoryType.valueOf("product"), id);
    }

    @Override
    @JSONField(serialize = false)
    public List<StockKey> getDependentStockKeys() {
        List<StockKey> dependentStockKeys = new ArrayList<>();
//        if (productPackages != null && !productPackages.isEmpty()) {
//            productPackages.forEach(pp -> {
//                Procurement procurement = pp.getProcurement();
//                StockKey stockKey = procurement.getStockKey();
//                stockKey.setOffset(pp.getOffset());
//                dependentStockKeys.add(stockKey);
//            });
//        }
//        if (tourismRouteList != null) {
//            tourismRouteList.forEach(tourismRoute -> {
//                BusNo busNo = tourismRoute.getBusNo();
//                StockKey stockKey = busNo.getStockKey();
//                stockKey.setOffset(tourismRoute.getOffset());
//                dependentStockKeys.add(stockKey);
//            });
//        }

        return dependentStockKeys;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public List<ProductPackage> getProductPackages() {
        return productPackages;
    }

    public void setProductPackages(List<ProductPackage> productPackages) {
        this.productPackages.clear();
        this.productPackages.addAll(productPackages);
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Boolean isShuttle() {
        return shuttle;
    }

    public void setShuttle(Boolean shuttle) {
        this.shuttle = shuttle;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }


    private Integer compareTourismRoute(TourismRoute a, TourismRoute b) {
        if (a.getOffset().equals(b.getOffset()))
            return a.getBusNo().getDepartTime() - b.getBusNo().getDepartTime();
        return a.getOffset() - b.getOffset();
    }

    private List<TourismRoute> sortTourismRoutes(List<TourismRoute> tourismRoutes) {
        return tourismRoutes.stream().sorted((a, b) -> compareTourismRoute(a, b)).collect(Collectors.toList());
    }

    public List<TourismRoute> getTourismRouteList() {
        // 这里要保证返回的list有序
        if (isTourismRouteListSorted)
            return tourismRouteList;
        setTourismRouteList(sortTourismRoutes(tourismRouteList));
        isTourismRouteListSorted = true;
        return tourismRouteList;
    }

    public void setTourismRouteList(List<TourismRoute> tourismRouteList) {
        this.tourismRouteList.clear();
        this.tourismRouteList.addAll(tourismRouteList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return that.getStockKey().equals(getStockKey());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result += InventoryType.product.hashCode();
        return result;
    }

    public boolean containBus() {
        return type.indexOf(Type.bus.name()) != -1;
    }
}

