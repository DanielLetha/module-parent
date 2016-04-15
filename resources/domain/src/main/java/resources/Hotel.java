package resources;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeff.Song on 2015/11/19.
 */
@Entity
@Table(name = "TR_HOTEL")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Hotel extends LogicalDeletableDomain implements IUnionEntityKey, IDependTracable {

    public enum StayType {
        hotel("酒店"), inn("客栈"), folk_house("民居"), others("其他");

        private String remark;

        StayType(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 住宿点名称
     */
    @Column
    private String name;

    /**
     * 住宿点类型
     */
    @Column
    @Enumerated(EnumType.STRING)
    private StayType type = StayType.hotel;

    /**
     * 详细地址
     */
    @Column
    private String address;

    /**
     * 目的地
     */
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     * 位置信息(经度)
     */
    @Column
    private BigDecimal lon;

    /**
     * 位置信息(纬度)
     */
    @Column
    private BigDecimal lat;

    /**
     * 备注信息
     * 由石广路添加于2015年12月24日，用以解决前端带格式的备注信息过长的问题
     */
    @Column(columnDefinition = "text")
    private String remark;

    @Version
    private Integer version;

    @Transient
    private static UnionEntityKey unionEntityKey;

    public Hotel() {
    }

    public Hotel(String name, String address, String remark, StayType type,
                 Destination destination, BigDecimal lon, BigDecimal lat) {
        this.name = name;
        this.address = address;
        this.remark = remark;
        this.type = type;
        this.destination = destination;
        this.lon=lon;
        this.lat=lat;
    }

    public Hotel(Long id, String name, String address, String remark, StayType type,
                 Destination destination, BigDecimal lon, BigDecimal lat) {
        this(name, address, remark, type, destination, lon, lat);
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StayType getType() {
        return type;
    }

    public void setType(StayType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
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
    @JSONField(serialize = false)
    public UnionEntityKey getUnionEntityKey() {
        if (null == name || null == destination) {
            return null;
        }

        if (null == unionEntityKey) {
            unionEntityKey = new UnionEntityKey(Hotel.class, name, destination);
        } else {
            unionEntityKey.setName(name);
            unionEntityKey.setDestination(destination);
        }

        return unionEntityKey;
    }

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencyList = new ArrayList<>();
        dependencyList.add(new Dependency(destination.getEntityKey()));
        return dependencyList;
    }
}
