package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.dialect.LongArrayUserType;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.*;

/**
 * 发车车次
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_BUSNO")
@TypeDefs({@TypeDef(name = "bigint[]", typeClass = LongArrayUserType.class)})
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class BusNo extends BaseDomain implements IStockTraceable {


    @Override
    @JSONField(serialize = false)
    public StockKey getStockKey() {
        return new StockKey(InventoryType.valueOf("bus_no"), id);
    }

    @Override
    @JSONField(serialize = false)
    public List<StockKey> getDependentStockKeys() {
        return Collections.emptyList();
    }

    public enum Status {
        normal("正常"), stop("停运");

        private String remark;

        Status(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }


    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 供应商编号
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 车次唯一编号，如T2014 成都往绵竹
     */
    @Column
    private String no;

    /**
     * 车次经过的目的地
     */
    @Type(type = "bigint[]")
    @Column(name = "destinations")
    private Long[] destinations;

    /**
     * 车次里程
     */
    @Column
    private Integer distance;

    /**
     * 车次历时
     */
    @Column
    private Integer duration;

    /**
     * 发车时间,相对发车当天北京时间00：00开始的秒数
     */
    @Column(name = "depart_time")
    private Integer departTime;

    /**
     * 抵达时间,相对发车当天北京时间00：00开始的秒数
     */
    @Column(name = "arrive_time")
    private Integer arriveTime;

    /**
     * 出发地
     */
    @Column
    private String depart;

    /**
     * 目的地
     */
    @Column
    private String arrive;

    /**
     * 是否往返
     */
    @Column
    private boolean shuttle = false;

    /**
     * 车次运行状态，normal:正常运行,stopped：停运，如果车次停运，则表示行程停运
     * 默认为normal
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.normal;

    /**
     * 可换乘
     */
    @Column
    private boolean transferable = false;

    @OrderBy("day ASC ,timing ASC ")
    @OneToMany(mappedBy = "busNo", cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Node> nodes;

    @Version
    private Integer version;


    /**
     * constructor
     */
    public BusNo() {
    }

    /**
     * constructor
     *
     * @param id
     */
    public BusNo(Long id) {
        this.id = id;
    }

    /**
     * constructor
     *
     * @param id
     * @param nodes
     */
    public BusNo(Long id, List<Node> nodes) {
        this.id = id;
        this.nodes = nodes;
    }

    /**
     * constructor
     *
     * @param no
     * @param distance
     * @param duration
     * @param departTime
     * @param arriveTime
     * @param depart
     * @param arrive
     * @param shuttle
     * @param status
     * @param transferable
     * @param nodes
     */
    public BusNo(String no, Integer distance, Integer duration, Integer departTime, Integer arriveTime, String depart, String arrive, boolean shuttle, Status status, boolean transferable, List<Node> nodes) {
        this.no = no;
        this.distance = distance;
        this.duration = duration;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.depart = depart;
        this.arrive = arrive;
        this.shuttle = shuttle;
        this.status = status;
        this.transferable = transferable;
        this.nodes = nodes;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long[] getDestinations() {
        return destinations;
    }

    public void setDestinations(Long[] destinations) {
        this.destinations = destinations;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Integer departTime) {
        this.departTime = departTime;
    }

    public Integer getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Integer arriveTime) {
        this.arriveTime = arriveTime;
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

    public boolean isShuttle() {
        return shuttle;
    }

    public void setShuttle(boolean shuttle) {
        this.shuttle = shuttle;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 某一辆车的容量情况
     */
    public static class BusCapacity {

        private Bus busId;             // 车辆

        private Integer capacity;       // 车辆容量

        private Integer usedCapacity;   // 车辆已消耗容量

        public BusCapacity() {
            this(null, 0, 0);
        }

        public BusCapacity(Bus busNo, Integer capacity, Integer usedCapacity) {
            this.busId = busNo;
            this.capacity = capacity;
            this.usedCapacity = usedCapacity;
        }

        public Bus getBusId() {
            return busId;
        }

        public void setBusId(Bus busId) {
            this.busId = busId;
        }

        public Integer getCapacity() {
            return capacity;
        }

        public void setCapacity(Integer capacity) {
            this.capacity = capacity;
        }

        public Integer getUsedCapacity() {
            return usedCapacity;
        }

        public void setUsedCapacity(Integer usedCapacity) {
            this.usedCapacity = usedCapacity;
        }
    }


    /**
     * 车次特定一段时间排班数据
     * 包含该这次一段时间内所有班次的容量数据
     */
    public static class BusNoSpanCapacity {

        private BusNo busNoId;

        private Integer offset;

        private boolean transferable;

        private Map<Date,List<BusCapacity>> busCapacities;    // 该车次对应的某一天的所有班次的容量信息

        public BusNoSpanCapacity() {
            this(null, 0, false, new HashMap<>());
        }

        public BusNoSpanCapacity(BusNo busNoId, Integer offset, boolean transferable) {
            this(busNoId, offset, transferable, new HashMap<>());
        }

        public BusNoSpanCapacity(BusNo busNoId, boolean transferable, Map<Date,List<BusCapacity>> busCapacities) {
            this(busNoId, 0, transferable, busCapacities);
        }

        public BusNoSpanCapacity(BusNo busNoId, Integer offset, boolean transferable, Map<Date,List<BusCapacity>> busCapacities) {
            this.busNoId = busNoId;
            this.offset = offset;
            this.transferable = transferable;
            this.busCapacities = busCapacities;
        }

        public BusNo getBusNoId() {
            return busNoId;
        }

        public void setBusNoId(BusNo busNoId) {
            this.busNoId = busNoId;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public boolean isTransferable() {
            return transferable;
        }

        public void setTransferable(boolean transferable) {
            this.transferable = transferable;
        }

        public Map<Date,List<BusCapacity>> getBusCapacities() {
            return busCapacities;
        }

        public void setBusCapacities(Map<Date,List<BusCapacity>> busCapacities) {
            this.busCapacities = busCapacities;
        }
    }

    /**
     * 车次特定某一天排班数据
     * 包含该这次某一天所有班次的容量数据
     */
    public static class BusNoCapacity {

        private BusNo busNoId;

        private Integer offset;

        private boolean transferable;

        private List<BusCapacity> busCapacities;    // 该车次对应的某一天的所有班次的容量信息

        public BusNoCapacity() {
            this(null, 0, false, new ArrayList<>());
        }

        public BusNoCapacity(BusNo busNoId, Integer offset, boolean transferable) {
            this(busNoId, offset, transferable, new ArrayList<>());
        }

        public BusNoCapacity(BusNo busNoId, boolean transferable, List<BusCapacity> busCapacities) {
            this(busNoId, 0, transferable, busCapacities);
        }

        public BusNoCapacity(BusNo busNoId, Integer offset, boolean transferable, List<BusCapacity> busCapacities) {
            this.busNoId = busNoId;
            this.offset = offset;
            this.transferable = transferable;
            this.busCapacities = busCapacities;
        }

        public BusNo getBusNoId() {
            return busNoId;
        }

        public void setBusNoId(BusNo busNoId) {
            this.busNoId = busNoId;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public boolean isTransferable() {
            return transferable;
        }

        public void setTransferable(boolean transferable) {
            this.transferable = transferable;
        }

        public List<BusCapacity> getBusCapacities() {
            return busCapacities;
        }

        public void setBusCapacities(List<BusCapacity> busCapacities) {
            this.busCapacities = busCapacities;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusNo busNo = (BusNo) o;

        if (shuttle != busNo.shuttle) return false;
        if (transferable != busNo.transferable) return false;
        if (no != null ? !no.equals(busNo.no) : busNo.no != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(destinations, busNo.destinations)) return false;
        if (distance != null ? !distance.equals(busNo.distance) : busNo.distance != null) return false;
        if (duration != null ? !duration.equals(busNo.duration) : busNo.duration != null) return false;
        if (departTime != null ? !departTime.equals(busNo.departTime) : busNo.departTime != null) return false;
        if (arriveTime != null ? !arriveTime.equals(busNo.arriveTime) : busNo.arriveTime != null) return false;
        if (depart != null ? !depart.equals(busNo.depart) : busNo.depart != null) return false;
        if (arrive != null ? !arrive.equals(busNo.arrive) : busNo.arrive != null) return false;
        if (status != busNo.status) return false;
        if (nodes != null ? !nodes.equals(busNo.nodes) : busNo.nodes != null) return false;
        return !(version != null ? !version.equals(busNo.version) : busNo.version != null);

    }

    @Override
    public int hashCode() {
        int result = no != null ? no.hashCode() : 0;
        result = 31 * result + (destinations != null ? Arrays.hashCode(destinations) : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (departTime != null ? departTime.hashCode() : 0);
        result = 31 * result + (arriveTime != null ? arriveTime.hashCode() : 0);
        result = 31 * result + (depart != null ? depart.hashCode() : 0);
        result = 31 * result + (arrive != null ? arrive.hashCode() : 0);
        result = 31 * result + (shuttle ? 1 : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (transferable ? 1 : 0);
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}



