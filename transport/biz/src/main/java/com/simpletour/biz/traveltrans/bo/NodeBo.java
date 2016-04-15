package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.Node;
import com.simpletour.domain.traveltrans.NodeType;
import resources.Destination;

/**
 * Created by mario on 2015/12/29.
 */
public class NodeBo {
    /**
     * ID
     */
    private Long id;

    /**
     * 节点类型编号
     */
    private NodeType nodeType;
    /**
     * 节点所在目的地编号
     * 包含目的地id，目的地名称，目的地详细地址,目的地经纬度
     */
    private Destination destination;
    /**
     * 地址
     */
    private String address;
    /**
     * 节点描述
     */
    private String description;
    /**
     * 行程第几天
     */
    private Integer day;

    /**
     * 到达时间
     */
    private Integer arriveTime;
    /**
     * 出发时间
     */
    private Integer departTime;
    /**
     * 版本号
     */
    private Integer version;

    /**
     * constructor
     */
    public NodeBo() {
    }

    /**
     * constructor
     *
     * @param nodeType
     * @param destination
     * @param address
     * @param description
     * @param day
     * @param arriveTime
     * @param departTime
     */
    public NodeBo(NodeType nodeType, Destination destination, String address, String description, Integer day, Integer arriveTime, Integer departTime) {
        this.nodeType = nodeType;
        this.destination = destination;
        this.address = address;
        this.description = description;
        this.day = day;
        this.arriveTime = arriveTime;
        this.departTime = departTime;
    }

    /**
     * constructor
     *
     * @param node
     */
    public NodeBo(Node node) {
        this.destination = node.getDestination();
        this.address = node.getAddress();
        this.nodeType = node.getNodeType();
        this.day = node.getDay();
        this.arriveTime = node.getTiming();
        this.departTime = node.getTiming() + node.getDuration();
        this.description = node.getDescr();
        this.id = node.getId();
        this.version = node.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Integer arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Integer getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Integer departTime) {
        this.departTime = departTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Node as(BusNo busNo) {
        Node node = new Node();
        node.setId(getId());
        node.setBusNo(busNo);
        node.setDay(getDay());
        node.setDuration(getDepartTime() - getArriveTime());
        node.setTiming(getArriveTime());
        node.setAddress(getAddress());
        node.setDestination(getDestination());
        node.setNodeType(getNodeType());
        node.setDescr(getDescription());
        node.setVersion(getVersion());
        return node;
    }
}
