package resources;

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
 * 娱乐、晚会信息
 * <p>
 * Created by Jeff.Song on 2015/11/19.
 */
@Entity
@Table(name = "TR_ENTERTAINMENT")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class Entertainment extends LogicalDeletableDomain implements IDependTracable {

    public enum Type{
        activity("活动"), others("其他");

        private  String remark;
        Type(String remark){
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    @Column(name = "tenant_id")
    private Long tenantId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 娱乐项目名称
     */
    @Column
    private String name;

    /**
     * 娱乐类型
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Type type = Type.activity;
    /**
     * 活动地点
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

    public Entertainment(){}

    public Entertainment(String name,Type type,String address,String remark,
                         Destination destination,BigDecimal lon,BigDecimal lat){
        this.name=name;
        this.type=type;
        this.address=address;
        this.remark=remark;
        this.destination=destination;
        this.lon=lon;
        this.lat=lat;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
    public List<Dependency> getDependencies() {
        List<Dependency> dependencyList = new ArrayList<>();
        dependencyList.add(new Dependency(destination.getEntityKey()));
        return dependencyList;
    }
}
