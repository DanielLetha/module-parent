package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2015/12/29.
 */
public class BusNoBo {
    /**
     * id
     */
    private Long id;

    /**
     * 车次唯一编号，如T2014 成都往绵竹
     */
    private String no;

    /**
     * 车次里程
     */
    private Integer distance;

    /**
     * 车次历时
     */
    private Integer duration;

    /**
     * 出发地
     */
    private String depart;

    /**
     * 目的地
     */
    private String arrive;

    /**
     * 发车时间,相对发车当天北京时间00：00开始的秒数
     */
    private Integer departTime;

    /**
     * 抵达时间,相对发车当天北京时间00：00开始的秒数
     */
    private Integer arriveTime;

    /**
     * 是否往返
     */
    private boolean shuttle;

    /**
     * 可换乘
     */
    private boolean transferable;

    /**
     * 车次运行状态，normal:正常运行,stopped：停运，如果车次停运，则表示行程停运
     * 默认为normal
     */
    private BusNo.Status status = BusNo.Status.normal;

    /**
     * 节点
     */
    private List<NodeBo> nodeBos;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * constructor
     */
    public BusNoBo() {
    }


    /**
     * setter& getter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setNo(String no) {
        this.no = no;
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

    public List<NodeBo> getNodeBos() {
        if (nodeBos == null) {
            nodeBos = new ArrayList<>();
        }
        return nodeBos;
    }

    public void setNodeBos(List<NodeBo> nodeBos) {
        this.nodeBos = nodeBos;
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

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public BusNo.Status getStatus() {
        return status;
    }

    public void setStatus(BusNo.Status status) {
        this.status = status;
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

    public BusNo as() {
        BusNo busNo = new BusNo();
        busNo.setVersion(getVersion());
        busNo.setId(getId());
        busNo.setNo(getNo());
        busNo.setDistance(getDistance());
        busNo.setStatus(getStatus());
        List<Node> nodes = nodeBos.stream().map(nodeBo -> nodeBo.as(busNo)).collect(Collectors.toList());
        busNo.setNodes(nodes);
        busNo.setDuration(toDuration(nodes));
        if (!(nodes == null || nodes.isEmpty())) {
            busNo.setDepartTime(nodes.get(0).getTiming());
            busNo.setArriveTime(nodes.get(nodes.size() - 1).getTiming() + nodes.get(nodes.size() - 1).getDay() * 86400);
        }
        busNo.setArrive(getArrive());
        busNo.setDepart(getDepart());
        busNo.setTransferable(isTransferable());
        busNo.setShuttle(isShuttle());
        return busNo;
    }

    public BusNoBo(BusNo busNo) {
        this.id = busNo.getId();
        this.no = busNo.getNo();
        this.version = busNo.getVersion();
        this.arrive = busNo.getArrive();
        this.depart = busNo.getDepart();
        this.shuttle = busNo.isShuttle();
        this.transferable = busNo.isTransferable();
        this.distance = busNo.getDistance();
        this.departTime = busNo.getDepartTime();
        this.arriveTime = busNo.getArriveTime();
        List<NodeBo> nodeBos = busNo.getNodes().stream().map(node -> new NodeBo(node)).collect(Collectors.toList());
        this.nodeBos = nodeBos;
    }

    private Integer toDuration(List<Node> nodes) {
        Integer start = nodes.get(0).getTiming();
        Integer end = nodes.get(nodes.size() - 1).getTiming() + nodes.get(nodes.size() - 1).getDay() * 86400;
        return (end - start);
    }

}
