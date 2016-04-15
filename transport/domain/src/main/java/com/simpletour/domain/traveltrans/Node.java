package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;
import resources.Destination;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * 车次行车节点
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_NODE")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class Node extends BaseDomain implements IDependTracable {


    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    /**
     * 车次编号
     */
    @ManyToOne
    @JoinColumn(name = "bus_no_id")
    @JSONField(serialize = false)
    private BusNo busNo;

    /**
     * 节点类型编号
     */
    @ManyToOne
    @JoinColumn(name = "type_id")
    private NodeType nodeType;

    /**
     * 节点所在目的地编号
     * 包含目的地id，目的地名称，目的地详细地址,目的地经纬度
     */
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    /**
     * 地址
     */
    @Column
    private String address;

    /**
     * 节点描述
     */
    @Column
    private String descr;

    /**
     * 行程第几天
     */
    @Column
    private Integer day;

    /**
     * 时间点
     */
    @Column
    private Integer timing;

    /**
     * 持续时间
     */
    @Column
    private Integer duration;


    @Version
    private Integer version;

    /**
     * constructor
     */
    public Node() {
    }

    /**
     * constructor
     *
     * @param busNo
     * @param nodeType
     * @param destination
     * @param descr
     * @param day
     * @param timing
     * @param duration
     */
    public Node(BusNo busNo, NodeType nodeType, Destination destination, String descr, Integer day, Integer timing, Integer duration) {
        this.busNo = busNo;
        this.nodeType = nodeType;
        this.destination = destination;
        this.descr = descr;
        this.day = day;
        this.timing = timing;
        this.duration = duration;
    }

    /**
     * constructor
     *
     * @param id
     * @param busNo
     * @param nodeType
     * @param destination
     * @param descr
     * @param day
     * @param timing
     * @param duration
     */
    public Node(Long id, BusNo busNo, NodeType nodeType, Destination destination, String descr, Integer day, Integer timing, Integer duration) {
        this.id = id;
        this.busNo = busNo;
        this.nodeType = nodeType;
        this.destination = destination;
        this.descr = descr;
        this.day = day;
        this.timing = timing;
        this.duration = duration;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BusNo getBusNo() {
        return busNo;
    }

    public void setBusNo(BusNo busNo) {
        this.busNo = busNo;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getDay() {
        return day == null ? 0 : day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getTiming() {
        return timing == null ? 0 : timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        return (id != null ? id.equals(node.id) : node.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }

//    @Override
//    @JSONField(serialize = false)
//    public List<DependEntity> getDependEntities() {
//        List<DependEntity> dependEntities = new ArrayList<>();
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(Node.class), this.id, QueryUtil.getTableName(BusNo.class), this.busNo.getId()));
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(Node.class), this.id, QueryUtil.getTableName(NodeType.class), this.nodeType.getId()));
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(Node.class), this.id, QueryUtil.getTableName(Destination.class), this.destination.getId()));
//        return dependEntities;
//    }

    @Override
    public List<Dependency> getDependencies() {
        return Arrays.asList(new Dependency(busNo.getEntityKey()),new Dependency(nodeType.getEntityKey()),new Dependency(destination.getEntityKey()));
    }
}
